# componentdev — the lab for *ZK Component Development Essentials*

Every code snippet in the `zk_component_dev_essentials` book is extracted from this project.
If a demo here is broken, the book is wrong.

- **ZK 10.3.0.1 CE, javax flavor, Java 11.** Pinned to a released version on purpose: the
  book backs a tool that generates projects for outside readers, and `-Eval`/`-FL`/`SNAPSHOT`
  builds are not resolvable for them.
- **Two client-side authoring tracks**, both exercised by the test suite (see below).

## Commands

```bash
W=/Users/hawk/Documents/workspace/toolbox/withjdk.sh

$W 11 mvn test        # server-side only: ZATS + plain JUnit. Fast, no browser.
$W 11 mvn verify      # THE GATE. Adds ZK WebDriver in headless Chrome.
$W 11 mvn jetty:run   # http://localhost:8080/componentdev/

npm install && npm run build   # recompile the TypeScript track (see below)
```

Always go through `withjdk.sh 11`. A bare `mvn` may select a JDK ZK 10 does not support.

## Layout

```
src/main/java/com/foo/                     server-side component + custom event
src/main/resources/metainfo/zk/lang-addon.xml   registers both component names
src/main/resources/web/js/com/foo/         PLAIN JS track (zk.$extends)
  SimpleLabel.js  zk.wpd  mold/  css/
src/main/ts/labts/                         TYPESCRIPT track source (@zk.WrapClass)
src/main/resources/web/js/labts/           ...and its committed tsc output
src/test/java/com/foo/                     the three test layers
src/test/webapp/                           the demo app + test fixtures
```

The directory contract under `src/main/resources` is the one the 9.6.0
`zk-archetype-component` established. The build is entirely new.

## The two tracks

`<simplelabel>` and `<simplelabelts>` are the **same server-side class**
(`com.foo.SimpleLabel`) with two different `widget-class` values. That makes "pick either
track" a testable claim rather than an assertion: `TrackParityIT` asserts both render
equivalent DOM and deliver the same `onClear` event with the same payload.

The TypeScript output is **committed**, so `mvn verify` tests the real compiled artifact
without requiring npm and without the Maven build shelling out to node. Re-run
`npm run build` after editing anything in `src/main/ts/`.

## The three test layers — and what each is blind to

| Layer | Pattern | Proves | Blind to |
|---|---|---|---|
| Plain JUnit | `*PropertyTest` | property logic on a **detached** component | anything needing a desktop |
| ZATS Mimic | `*Test` | lang-addon resolution, property round-trip, mold selection, `service()`, event posting | **never renders, never runs JS** |
| ZK WebDriver | `*IT` | mold DOM, `$s()` classes, `$n()` lookups, `bind_`/`unbind_`, the real round trip | slow; needs local Chrome |

Both ZATS and ZK WebDriver are free and part of ZK CE.

Choosing the wrong layer produces a test that passes while the documented behavior is
broken. The canonical case: a property that is `smartUpdate`-ed but never rendered in
`renderProperties()` passes **every** ZATS test and fails only on first paint in a browser.
This was verified by deliberately breaking it — see below.

## Fail-closed evidence

A suite that cannot fail is worth nothing, so the suite was run against a deliberately
broken lab. All four mutations turned it red, each with the matching test failing:

| Mutation | Result |
|---|---|
| mold emits `-innr` instead of `-inner` | 2 IT tests fail |
| `renderProperties()` drops the value | 2 tests fail (**invisible to ZATS**) |
| `addClientEvent(...)` removed | the click round-trip IT fails |
| `setValue` null-normalization removed | the property test fails |

## Traps this project already pays for

- **`tsconfig` must target `es5`.** ZK instantiates a widget class by *calling* it; a native
  ES2015 class throws `Class constructor ... cannot be invoked without 'new'`. ZK's own
  tsconfig targets es5.
- **Four dependency version skews are pinned** in `dependencyManagement` (`jetty-bom`,
  `guava`, `slf4j-api`, `httpcore5`). ZK's compile-scope dependencies sit *shallower* than
  the test-scope ones, so Maven's nearest-wins silently downgrades them. The `httpcore5` one
  surfaces as `NoClassDefFoundError: Could not initialize class ChromiumHeadlessDriver` on
  every browser test — read the *earliest* stack trace, not the most frequent.
- **`jq("$id")` resolves a ZK component id only.** `jq("$greet-inner")` matches nothing; to
  reach a sub-element resolve the uuid first.
- **ZK swallows widget-loading errors** — it logs to the console and marks the package
  loaded. A broken `zk.wpd` renders an empty element with no exception, which is why every
  browser test ends with `assertNoJSError()`.
- **No `*.dsp` servlet mapping is needed** in `web.xml`. A `.css.dsp` named by `<css-uri>` is
  server-side-included into the language's single `~./zul/css/zk.wcs` response and interpreted
  in-process, so the container never sees a `.dsp` request. Proven by deleting both the mapping
  and the `zweb-dsp` dependency and observing a byte-identical aggregated response.
- **The two tracks live on separate fixtures.** One JS error stops a ZK page's AU cycle, so a
  broken widget on a shared fixture fails every other test on that page too.
