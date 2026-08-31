# uitest — browser-driven checks for the Component Reference pages

These scripts open each demo page in headless Chrome, click and type through it with real
input events, and assert what the component actually did. They exist because a screenshot
cannot tell you whether `onClose` detached a chip, whether `onChanging` carried the right
indices, or whether a confirmpopup deleted the row that opened it — and because three
statements on these pages turned out to be wrong until something clicked them.

Not wired into Maven: no `mvn test` phase, no dependencies, nothing to install. Plain Node.

## Running

```bash
# 1. serve the pages
cd componentreference
withjdk.sh 11 mvn jetty:run -Djetty.port=8090 -Dhttps.port=8453

# 2. in another shell
cd componentreference/uitest
node test-carousel.js     # 13 assertions
node test-rest.js         # 44 assertions over the other six pages
```

Exit code is the number of failed assertions, so `node test-rest.js && echo ok` works in a
script. Every page also gets a "no JS errors" assertion from the console and exception feed.

Environment overrides:

| Variable | Default |
|---|---|
| `ZK_BASE` | `http://localhost:8090/component` |
| `CHROME_BIN` | `/Applications/Google Chrome.app/Contents/MacOS/Google Chrome` |
| `CDP_PORT` | `9222` |

Requires Node 18+ for `fetch` and Node 22+ for the global `WebSocket` (no `ws` package).

## Files

- `cdp.js` — the driver: launches Chrome, talks the DevTools Protocol over a WebSocket, and
  offers `eval`, `waitFor`, `click`, `clickAt`, `key`, `shot`, plus `check`/`report`/`summary`.
- `test-carousel.js` — carousel and carouselitem.
- `test-rest.js` — confirmpopup, chip, avatargroup, badge, breadcrumb, avatar.

## Things that will bite you when writing more of these

- **Notifications stack.** ZK appends every `Clients.showNotification` as a new
  `.z-notification` node and leaves the old ones in the DOM, so read the **last** one, not the
  first. Never delete the nodes to "reset" — that desynchronises the widget and the next
  notification silently fails to appear.
- **Scroll before clicking.** A synthetic mouse event at coordinates outside the viewport is
  dropped without error. `click()` scrolls the element into view first and then refuses to
  click anything still off-screen, instead of failing mysteriously later.
- **Confirmpopup focuses on a timer.** Pressing Enter right after `open()` lands nowhere.
  Wait for `.z-confirmpopup-ok` to hold the focus first.
- **There is no `zk.$$` in ZK 11.** To find a widget by its ZUL id, walk
  `document.querySelectorAll('[id]')` and compare `zk.Widget.$(el).id` — that is what the
  injected `zkWidget()` / `zkNode()` helpers do.
- **Some widgets render lazily.** A `menupopup` is not in the DOM at all until it is first
  opened.
