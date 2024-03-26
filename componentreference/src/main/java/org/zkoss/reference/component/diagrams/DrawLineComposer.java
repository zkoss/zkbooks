package org.zkoss.reference.component.diagrams;

import org.zkoss.gmaps.*;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;

import java.util.*;

public class DrawLineComposer extends SelectorComposer {
    @Wire
    private Gmaps map4draw;

    private List<LatLng> clickedPoints = new LinkedList();

    private Gpolyline currentPolyline = new Gpolyline();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    @Listen("onCreate = #map4draw")
    public void onCreate(){
        map4draw.appendChild(currentPolyline);
    }

    @Listen("onMapClick = #map4draw")
    public void recordDraw(MapMouseEvent event){
        clickedPoints.add(event.getLatLng());
        drawPolylines();
    }

    @Listen("onClick = #clear")
    public void clearPoints(){
        clickedPoints.clear();
        currentPolyline.setPath(new LinkedList<>(clickedPoints));
    }

    private void drawPolylines() {
        if (clickedPoints.size() > 1) {
            currentPolyline.setPath(new LinkedList<>(clickedPoints));
        }
    }
}
