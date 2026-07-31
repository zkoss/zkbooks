/* SimpleLabel.js

	The client-side half of com.foo.SimpleLabel - the PLAIN JS track (zk.$extends).

	This is the default authoring track in the book: no build step, no npm, no tsc.
	The TypeScript equivalent lives in src/main/ts/ and produces an identical DOM
	and identical events, so the two tracks can be compared directly.
*/
com.foo.SimpleLabel = zk.$extends(zul.Widget, {
	_value: '',
	_cleared: false,

	$define: {
		/**
		 * The label text.
		 *
		 * $define generates getValue() and setValue() for us. The function below is
		 * the POST-assignment hook: it runs after this._value has already been set,
		 * and its job is to push the new value into the DOM.
		 *
		 * Patching the sub-node is much cheaper than this.rerender(), and it keeps
		 * focus and scroll position intact.
		 */
		value: function () {
			var n = this.$n('inner'); // finds id="<uuid>-inner", emitted by the mold
			if (n)
				n.innerHTML = zUtl.encodeXML(this._value);
		}
	},

	/**
	 * Called when the widget is attached to the DOM.
	 *
	 * Register DOM listeners here, NOT in the constructor: before bind_ there is no
	 * DOM node to listen to.
	 */
	bind_: function () {
		this.$supers('bind_', arguments);
		// The 3rd argument is the NAME of a method on this widget. It must be
		// spelled identically in domUnlisten_ below or the listener leaks.
		this.domListen_(this.$n(), 'onClick', '_doClear');
	},

	/**
	 * Called when the widget is detached from the DOM.
	 *
	 * Mirror bind_ exactly, in reverse order. Anything bind_ created and unbind_
	 * forgets to release is a leak that survives every page navigation.
	 */
	unbind_: function () {
		this.domUnlisten_(this.$n(), 'onClick', '_doClear');
		this.$supers('unbind_', arguments);
	},

	/** Toggles the text and tells the server about it. */
	_doClear: function (evt) {
		this._cleared = !this._cleared;

		var n = this.$n('inner');
		if (n)
			n.innerHTML = this._cleared ? '' : zUtl.encodeXML(this._value);

		// Send the custom event to the server. The name must match
		// ClearEvent.NAME and the addClientEvent declaration in SimpleLabel.java,
		// or the request is dropped on arrival with no error.
		this.fire('onClear', {cleared: this._cleared});
	}
});
