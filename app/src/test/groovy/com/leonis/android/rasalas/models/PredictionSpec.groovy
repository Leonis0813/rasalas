package com.leonis.android.rasalas.models

import org.json.JSONObject
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
/**
 * Created by leonis on 2018/09/24.
 *
 */
class PredictionSpec extends Specification {
    @Shared jsonObject

    def setupSpec() {
        jsonObject = new JSONObject()
        jsonObject.put("created_at", "1000-01-01T09:30:00.000Z")
        jsonObject.put("pair", "USDJPY")
        jsonObject.put("result", "up")
    }

    @Unroll
    def "getDatetime"() {
        given:
        def prediction = new Prediction(jsonObject)
        def calendar = Calendar.getInstance()

        when:
        def datetime = prediction.getDatetime()
        calendar.setTime(datetime)

        then:
        calendar.get(Calendar.YEAR) == 1000
        calendar.get(Calendar.MONTH) == 0
        calendar.get(Calendar.DATE) == 1
        calendar.get(Calendar.HOUR) == 9
        calendar.get(Calendar.MINUTE) == 30
        calendar.get(Calendar.SECOND) == 0
    }

    @Unroll
    def "getPair"() {
        when:
        def prediction = new Prediction(jsonObject)

        then:
        prediction.pair == "USDJPY"
    }

    @Unroll
    def "getResult"() {
        when:
        def prediction = new Prediction(jsonObject)

        then:
        prediction.result == "up"
    }
}
