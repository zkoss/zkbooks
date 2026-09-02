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
node test-carousel.js       # 14 assertions
node test-rest.js          # 51 assertions over the other six pages
node test-daterangebox.js  # 21 assertions over the three daterangebox use cases
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
- `test-daterangebox.js` — the ERP, HR and plant-maintenance use cases on
  `input/daterangebox.zul`.

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
- **`.z-label` sets its own `font-size`.** Sizing a label by styling the wrapper around it
  does nothing — the label's own rule wins over the inherited value, so the size has to go on
  the label. This is silent: the text just renders at the theme size, and on the KPI board it
  left the unit bigger than the number it belonged to.
- **Layout components clip.** `.z-hlayout` sets `overflow: hidden`, so anything a child draws
  outside its own box — a wrap-mode badge indicator sits 10px past the corner — is silently
  cut off. The badge checks walk every ancestor with a clipping `overflow` and compare
  rectangles, because the page still renders and screenshots still look plausible.
- **Some labels contain `&nbsp;`, not spaces.** A comboitem renders "Tom Wu" as `Tom&nbsp;Wu`,
  so matching on a typed space finds nothing and looks exactly like a missing element. The
  `byText` / `byTextContains` / `textOf` helpers normalise U+00A0 through `normText()`; use that
  rather than raw `textContent` when you compare label text yourself.
- **Scrolling closes an open float.** `click()` scrolls its target into view first, which
  dismisses an already-open combobox popup — so the click then fails with "element not found".
  Scroll the *owner* into view, open the float, then `rect()` the item and `clickAt()` its
  coordinates.
- **Calendar cells are addressed by `aria-label`.** A day in a daterangebox popup renders
  as `<td aria-label="7 September, 2026">`, which is the only stable handle — the cell ids
  are auto-generated and the visible text is just the day number, repeated once per panel.
  The popup itself is appended to `body`, so pick the one that is displayed rather than the
  one that belongs to the widget.
- **A typed date commits on blur, not on Enter.** Typing into a daterangebox input and
  pressing Enter leaves the text in the field and fires nothing; Tab commits it, and that is
  also the only cheap way to ask for a range wide enough to violate `maxNights`.
- **A second error box does not get the `-open` class.** While one `.z-errorbox` is still
  showing, the next one is inserted as a plain `.z-errorbox` — visible, but not matched by
  `.z-errorbox-open`. Match on the message text instead.
- **Scope lookups that could match a second time.** `byTextContains('.z-chip', 'Jane Chen')`
  hits the demo chip near the top of `chip.zul`, not the one in the use case; the recipient
  checks search inside `zkNode('recipientBar')` instead.
