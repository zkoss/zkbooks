/**
 * Purpose: only mark today or user selected day on the pop-up calendar, not other days
 * Based on version: 8.6.1
 */
zk.afterLoad('zul.db', function() {
    var exWidget = {};
    zk.override(zul.db.Calendar.prototype, exWidget, {
		_markCal0: function(opts){
			exWidget._markCal0.apply(this, arguments);
            if (!this.isTodayOrUserSelectedDay()){
                //remove selected mark
                var selectedDay_class = this.$s('selected');
                jq('.'+selectedDay_class, this.$n('mid')).removeClass(selectedDay_class)
            }
		},
		isTodayOrUserSelectedDay: function(){
            var calendarDate = this.getTime();
            var today = zUtl.today(null, this.getTime().getTimeZone());
            var userSelectedDate = this.parent.getValue() || today;
            return userSelectedDate.getMonth() == calendarDate.getMonth() &&
                userSelectedDate.getFullYear() == calendarDate.getFullYear();
		}
    });
});