/* SimpleLabel.ts -- the ADVANCED (TypeScript) authoring track.
 *
 * This is the same widget as web/js/com/foo/SimpleLabel.js, written the way ZK 10
 * writes its own widgets: a TypeScript class with the @zk.WrapClass decorator instead
 * of zk.$extends. It is paired with the SAME server-side class (com.foo.SimpleLabel)
 * under a second component name, so the two tracks are provably interchangeable - the
 * test suite asserts both produce equivalent DOM and the same server events.
 *
 * Two things here will bite anyone porting a widget from the plain-JS track:
 *
 * 1. The class is deliberately NOT exported. @zk.WrapClass registers it as
 *    window.labts.SimpleLabel by itself. Adding `export` turns this file into a module;
 *    tsc would then emit `exports.SimpleLabel = ...`, which throws
 *    "exports is not defined" when the WPD inlines it. With this tsconfig you get a
 *    compile error (TS2686) instead, which is the guardrail working as intended.
 *
 * 2. Use `super.method()`, NOT `$supers(Class, 'method', arguments)`. tsc's own inline
 *    __extends does not set the `_$super` property that the class-argument form of
 *    $supers relies on - ZK adds that in a build-time replacement helper (extends.js,
 *    ZK-5441) that a plain tsc project does not get. `super.method()` compiles to
 *    `_super.prototype.method.call(this, ...)` and works regardless.
 *
 * The tsconfig targets es5, and that is load-bearing rather than conservative: ZK
 * instantiates a widget class by CALLING it, and a native ES2015 class throws
 * "Class constructor SimpleLabel cannot be invoked without 'new'". ZK's own
 * tsconfig.json targets es5 for the same reason. See tsconfig.json for the detail.
 */
@zk.WrapClass('labts.SimpleLabel')
class SimpleLabel extends zul.Widget {
	_value = '';
	_cleared = false;

	setValue(value: string): void {
		this._value = value;
		const n = this.$n('inner');
		if (n)
			n.innerHTML = zUtl.encodeXML(value);
	}

	getValue(): string {
		return this._value;
	}

	override bind_(desktop?: zk.Desktop, skipper?: zk.Skipper, after?: CallableFunction[]): void {
		super.bind_(desktop, skipper, after);
		this.domListen_(this.$n()!, 'onClick', '_doClear');
	}

	override unbind_(skipper?: zk.Skipper, after?: CallableFunction[], keepRod?: boolean): void {
		this.domUnlisten_(this.$n()!, 'onClick', '_doClear');
		super.unbind_(skipper, after, keepRod);
	}

	/** Mirrors the plain-JS track's _doClear exactly, so the tests can assert parity. */
	_doClear(): void {
		this._cleared = !this._cleared;

		const n = this.$n('inner');
		if (n)
			n.innerHTML = this._cleared ? '' : zUtl.encodeXML(this._value);

		this.fire('onClear', {cleared: this._cleared});
	}
}
