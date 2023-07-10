package com.lipanre.snakeyaml.config;

import com.lipanre.snakeyaml.annotation.PropertiesConfiguration;
import com.lipanre.snakeyaml.annotation.PropertiesField;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Layton
 * @date 2022/9/5 16:44
 */
@Data
@PropertiesConfiguration
public class ShineMonitorAppConfiguration {

    @PropertiesField(value = "http.sal.url")
    private String salUrl;

    @PropertiesField(value = "http.sal.count", type = Integer.class)
    private Integer salCount;

    @PropertiesField(value = "http.sal.*.userAccount", type = List.class)
    private List<String> userAccountList;

    @PropertiesField(value = "http.sal.*.userPassword", type = List.class)
    private List<String> userPasswordList;

    @PropertiesField(value = "http.sal.companyKey")
    private String companyKey;

    @PropertiesField(value = "http.sal.source")
    private String source;

    @PropertiesField(value = "http.sal.appId")
    private String appId;

    @PropertiesField(value = "http.sal.privateKey")
    private String privateKey;

    @PropertiesField(value = "http.sal.appVersion")
    private String appVersion;

    @PropertiesField(value = "http.sal.appClient")
    private String appClient;

    @PropertiesField(value = "http.inverter.status.desc")
    private String inverterStatusDesc;

    @PropertiesField(value = "http.inverter.status.value")
    private String inverterStatusValue;

    @PropertiesField(value = "http.inverter.alarm.desc")
    private String inverterAlarmDesc;

    @PropertiesField(value = "http.inverter.alarm.value")
    private String inverterAlarmValue;

    @PropertiesField(value = "http.voltage.mapping.count", type = Integer.class)
    private Integer powerCount;

    @PropertiesField(value = "http.voltage.mapping.*.power", type = List.class)
    private List<String> powerList;

    @PropertiesField(value = "http.voltage.mapping.*.mppt", type = List.class)
    private List<String> mpptList;

    @PropertiesField(value = "http.voltage.mapping.*.series", type = List.class)
    private List<String> seriesList;

    public static final String VOLTAGE_NAME = "输入电压";

    private final Map<String, Integer> inverterStatusMap = new HashMap<>();
    private final Map<String, Integer> inverterAlarmMap = new HashMap<>();

    private final Map<String, Map<String, List<String>>> mpptSeriesMap = new HashMap<>();

    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public void init() {
        inverterStatusMap.clear();
        if (!isEmpty(inverterStatusDesc) && !isEmpty(inverterStatusValue)) {
            try {
                String[] statusDesc = inverterStatusDesc.split(",");
                String[] statusValue = inverterStatusValue.split(",");
                for (int i = 0; i < statusDesc.length; i++) {
                    inverterStatusMap.put(statusDesc[i], Integer.parseInt(statusValue[i]));
                }
            } catch (Exception e) {
                // ignore
                e.printStackTrace();
            }
        }

        inverterAlarmMap.clear();
        if (!isEmpty(inverterAlarmDesc) && !isEmpty(inverterAlarmValue)) {
            try {
                String[] statusDesc = inverterAlarmDesc.split(",");
                String[] statusValue = inverterAlarmValue.split(",");
                for (int i = 0; i < statusDesc.length; i++) {
                    inverterAlarmMap.put(statusDesc[i], Integer.parseInt(statusValue[i]));
                }
            } catch (Exception e) {
                // ignore
                e.printStackTrace();
            }
        }

        try {
            for (int i = 0; i < this.powerCount; i++) {
                String[] powerArr = powerList.get(i).split(",");
                String[] mpptArr = mpptList.get(i).split(",");
                String[] seriesArr = seriesList.get(i).split(";");
                for (String power : powerArr) {
                    Map<String, List<String>> powerMap = mpptSeriesMap.computeIfAbsent(power, k -> new HashMap<>());
                    for (int j = 0; j < mpptArr.length; j++) {
                        String mpptKey = mpptArr[j] + VOLTAGE_NAME;
                        String seriesKey = seriesArr[j];
                        powerMap.put(mpptKey, Arrays.stream(seriesKey.split(",")).collect(Collectors.toList()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getStatusValueByDesc(String desc) {
        return inverterStatusMap.get(desc);
    }

    public Integer getAlarmValueByDesc(String desc) {
        return inverterAlarmMap.get(desc);
    }
}
