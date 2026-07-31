"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
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
var SimpleLabel = /** @class */ (function (_super) {
    __extends(SimpleLabel, _super);
    function SimpleLabel() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this._value = '';
        _this._cleared = false;
        return _this;
    }
    SimpleLabel.prototype.setValue = function (value) {
        this._value = value;
        var n = this.$n('inner');
        if (n)
            n.innerHTML = zUtl.encodeXML(value);
    };
    SimpleLabel.prototype.getValue = function () {
        return this._value;
    };
    SimpleLabel.prototype.bind_ = function (desktop, skipper, after) {
        _super.prototype.bind_.call(this, desktop, skipper, after);
        this.domListen_(this.$n(), 'onClick', '_doClear');
    };
    SimpleLabel.prototype.unbind_ = function (skipper, after, keepRod) {
        this.domUnlisten_(this.$n(), 'onClick', '_doClear');
        _super.prototype.unbind_.call(this, skipper, after, keepRod);
    };
    /** Mirrors the plain-JS track's _doClear exactly, so the tests can assert parity. */
    SimpleLabel.prototype._doClear = function () {
        this._cleared = !this._cleared;
        var n = this.$n('inner');
        if (n)
            n.innerHTML = this._cleared ? '' : zUtl.encodeXML(this._value);
        this.fire('onClear', { cleared: this._cleared });
    };
    SimpleLabel = __decorate([
        zk.WrapClass('labts.SimpleLabel')
    ], SimpleLabel);
    return SimpleLabel;
}(zul.Widget));
