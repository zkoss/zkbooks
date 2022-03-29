package org.zkoss.reference.developer.internationalization;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.Searchbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Radiogroup;

public class DateboxTimezoneComposer extends SelectorComposer<Component> {

	@Wire
	private Datebox userInputDatebox;
	@Wire
	private Searchbox<String> timeZoneSearchBox;
	@Wire
	private Label userLocal, userLocalTimeZone, userLocalGmtOffset, userToGmt, userToUser, userLocalToUser, userToServer, serverLocal, serverLocalGmtOffset, serverToGmt, serverToUser, serverToServer;
	
	private ZoneId clientZoneId;
	private ZoneId serverZoneId = ZoneId.of("GMT+4");
	
	private SimpleDateFormat userSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat serverSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat gmtSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ListModelList<String> timeZoneSeachModel;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		userInputDatebox.setFormat("yyyy-MM-dd HH:mm:ss");
		userInputDatebox.setLenient(true);
		userInputDatebox.setTimeZonesReadonly(true);
		userInputDatebox.setReadonly(true);
		timeZoneSeachModel = new ListModelList<String>(TimeZone.getAvailableIDs());
		timeZoneSeachModel.setMultiple(false);
		timeZoneSearchBox.setModel(timeZoneSeachModel);
		
	}
	
	@Listen(Events.ON_CLIENT_INFO + "=#main")
	public void handleClientInfo(ClientInfoEvent event) {
		clientZoneId = event.getZoneId();
		timeZoneSeachModel.addToSelection(clientZoneId.getId());
		userSimpleDateFormat.setTimeZone(TimeZone.getTimeZone(clientZoneId));
		userInputDatebox.setDisplayedTimeZones(Arrays.asList(TimeZone.getTimeZone(clientZoneId)));
		userInputDatebox.setTimeZone(TimeZone.getTimeZone(clientZoneId));
		updateAllTimeStamps();
	}
	
	@Listen(Events.ON_SELECT + "=#timeZoneSearchBox")
	public void handleLocalTimeZoneChange(SelectEvent<Searchbox<String>, String> event) {
		Iterator<String> valueIterator = event.getSelectedObjects().iterator();
		if(valueIterator.hasNext()) {
			clientZoneId = ZoneId.of(valueIterator.next());
			userSimpleDateFormat.setTimeZone(TimeZone.getTimeZone(clientZoneId));
			userInputDatebox.setDisplayedTimeZones(Arrays.asList(TimeZone.getTimeZone(clientZoneId)));
			userInputDatebox.setTimeZone(TimeZone.getTimeZone(clientZoneId));
		}
		updateAllTimeStamps();
	}
	
	@Listen(Events.ON_CHANGE + "=#userInputDatebox")
	public void handleUserInputDateboxChange() {
		updateAllTimeStamps();
	}
	
	@Listen(Events.ON_TIMER + "=#uiUpdateTimer")
	public void handleUpdateTimer() {
		updateAllTimeStamps();
	}

	@Listen(Events.ON_CLICK + "=#cleardb")
	public void handleClearBtn() {
		userInputDatebox.setValue(null);
		updateAllTimeStamps();
	}
	
	@Listen(Events.ON_CHECK + "=#timeControlCheck")
	public void handleTimeControlCheck(CheckEvent event) {
		userInputDatebox.setFormat(event.isChecked()?"yyyy-MM-dd HH:mm:ss":"yyyy-MM-dd");
		updateAllTimeStamps();
	}
	

	
	private void updateAllTimeStamps() {
		Date userValue = userInputDatebox.getValue();
		serverSimpleDateFormat.setTimeZone(TimeZone.getTimeZone(serverZoneId.getId()));
		userSimpleDateFormat.setTimeZone(TimeZone.getTimeZone(clientZoneId));
		gmtSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));

		userLocal.setValue(clientZoneId.getId());
		userLocalTimeZone.setValue(TimeZone.getTimeZone(clientZoneId).getDisplayName());
		userLocalGmtOffset.setValue(ZonedDateTime.ofInstant(Instant.now(), clientZoneId).getOffset().toString());
		userToUser.setValue((userValue!=null)?userSimpleDateFormat.format(userValue):"unset");
		userLocalToUser.setValue((userValue!=null)?localDateTimeToString(userInputDatebox.getValueInLocalDateTime()):"unset");
		userToServer.setValue((userValue!=null)?serverSimpleDateFormat.format(userValue):"unset");
		userToGmt.setValue((userValue!=null)?gmtSimpleDateFormat.format(userValue):"unset");
		
		serverLocal.setValue(serverZoneId.getId());
		Date localDate = new Date();
		serverLocalGmtOffset.setValue(ZonedDateTime.ofInstant(Instant.now(), serverZoneId).getOffset().toString());
		serverToGmt.setValue(gmtSimpleDateFormat.format(localDate));
		serverToUser.setValue(userSimpleDateFormat.format(localDate));
		serverToServer.setValue(serverSimpleDateFormat.format(localDate));
	}

	private String localDateTimeToString(LocalDateTime valueInLocalDateTime) {
		StringBuilder sb = new StringBuilder();
		sb.append("Year: ");
		sb.append(valueInLocalDateTime.getYear());
		sb.append(", Month: ");
		sb.append(valueInLocalDateTime.getMonth());
		sb.append(", Day of Month: ");
		sb.append(valueInLocalDateTime.getDayOfMonth());
		sb.append(", Hour: ");
		sb.append(valueInLocalDateTime.getHour());
		sb.append(", Minute: ");
		sb.append(valueInLocalDateTime.getMinute());
		sb.append(" Second: ");
		sb.append(valueInLocalDateTime.getSecond());
		return sb.toString();
	}
	
}