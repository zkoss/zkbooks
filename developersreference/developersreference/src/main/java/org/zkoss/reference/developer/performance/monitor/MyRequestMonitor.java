package org.zkoss.reference.developer.performance.monitor;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.util.PerformanceMeter;

import javax.servlet.http.*;
import java.util.HashMap;

public class MyRequestMonitor implements PerformanceMeter {

    //key is request ID
    private HashMap<String, Long> clientReceiveTimeRecord = new HashMap<>();
    private HashMap<String, Long> clientStartTimeRecord = new HashMap<>();
    private HashMap<String, Long> serverStartTimeRecord = new HashMap<>();
    private HashMap<String, Long> serverCompleteTimeRecord = new HashMap<>();

    /**
     * only be called when handling an AU request, not a request to a zul
     */
    @Override
    public void requestStartAtClient(String requestId, Execution exec, long time) {
        clientStartTimeRecord.put(requestId, time);
//        log("requestStartAtClient", requestId);

    }

    /**
     * one request contains the previous request's receiving and complete time.
     */
    @Override
    public void requestReceiveAtClient(String requestId, Execution exec, long time) {
        clientReceiveTimeRecord.put(requestId, time);
//        log("requestReceiveAtClient", requestId);
    }

    @Override
    public void requestCompleteAtClient(String requestId, Execution exec, long time) {
        log("client execution", requestId, exec, time - clientReceiveTimeRecord.get(requestId));
        if (clientStartTimeRecord.containsKey(requestId)) {
            log("network latency", requestId, exec,
                    clientReceiveTimeRecord.get(requestId) - serverCompleteTimeRecord.get(requestId) +
                            serverStartTimeRecord.get(requestId) - clientStartTimeRecord.get(requestId));
        }
        clearRecord(requestId);
//        log("requestCompleteAtClient", requestId);
    }

    private void clearRecord(String requestId) {
        serverStartTimeRecord.remove(requestId);
        serverCompleteTimeRecord.remove(requestId);
        clientStartTimeRecord.remove(requestId);
        clientReceiveTimeRecord.remove(requestId);
    }

    @Override
    public void requestStartAtServer(String requestId, Execution exec, long time) {
        serverStartTimeRecord.put(requestId, time);
//        log("requestStartAtServer", requestId);
    }

    @Override
    public void requestCompleteAtServer(String requestId, Execution exec, long time) {
        log("server execution", requestId, exec, time - serverStartTimeRecord.get(requestId));
        serverCompleteTimeRecord.put(requestId, time);
//        log("requestCompleteAtServer", requestId);
    }

    private void log(String name, String requestId, Execution exec, long duration){
        System.out.format("%s - %s - %s time: %s ms\n",
                requestId,
                ((HttpServletRequest)exec.getNativeRequest()).getRequestURI(),
                name,
                duration);
    }

    private void log(String name, String requestId){
        System.out.format("%s - %s\n",
                requestId, name);
    }
}
