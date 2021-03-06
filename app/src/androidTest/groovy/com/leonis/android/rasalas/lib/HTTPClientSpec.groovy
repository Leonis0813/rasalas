package com.leonis.android.rasalas.lib

import android.support.test.runner.AndroidJUnit4

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static android.support.test.InstrumentationRegistry.getContext
import static org.junit.Assert.assertEquals
import static org.junit.Assert.fail
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

/**
 * Created by leonis on 2019/09/24.
 */

@RunWith(AndroidJUnit4.class)
class HTTPClientSpec {
    def ret, con, responseBody

    @Before
    void setUp() throws Exception {
        con = mock(HttpURLConnection.class)
        doNothing().when(con).connect()
        responseBody = new JSONObject()
    }

    @Test
    void testGetPredictions_OK() {
        def httpClient = new HTTPClient(getContext())
        httpClient.getPredictions("1", "USDJPY")
        def predictions = new JSONArray()
        def prediction = new JSONObject()
        def expected = new HashMap<String, String>() {
            {
                put("prediction_id", "60a830b4b980f68ab0828517f806e680")
                put("model", "model.zip")
                put("from", "2019-08-01T00:00:00.000Z")
                put("to", "2019-08-01T23:59:59.000Z")
                put("pair", "USDJPY")
                put("means", "auto")
                put("result", "up")
                put("state", "completed")
                put("created", "2019-08-02T09:00:00.000Z")
            }
        }

        try {
            expected.entrySet().each { def entry ->
                prediction.put(entry.getKey(), entry.getValue())
            }
            predictions.put(prediction)
            responseBody.put("predictions", predictions)
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 200, responseBody.toString())

        ret = httpClient.loadInBackground()

        assertEquals(200, Integer.parseInt(ret.get("statusCode").toString()))

        try {
            responseBody = new JSONObject(ret.get("body").toString())
            predictions = responseBody.getJSONArray("predictions")
            prediction = predictions.getJSONObject(0)
            expected.entrySet().each { def entry ->
                assertEquals(entry.getValue(), prediction.getString(entry.getKey()))
            }
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
    }

    @Test
    void testGetPredictions_NG_invalidPage() {
        def httpClient = new HTTPClient(getContext())
        httpClient.getPredictions("invalid", "USDJPY")
        def errors = new JSONArray()
        def error = new JSONObject()

        try {
            error.put("error_code", "invalid_params_page")
            errors.put(error)
            responseBody.put("errors", errors)
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 400, responseBody.toString())

        ret = httpClient.loadInBackground()

        assertEquals(400, Integer.parseInt(ret.get("statusCode").toString()))

        try {
            responseBody = new JSONObject(ret.get("body").toString())
            errors = responseBody.getJSONArray("errors")
            error = errors.getJSONObject(0)

            assertEquals("invalid_params_page", error.getString("error_code"))
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
    }

    @Test
    void testGetPredictions_NG_invalidPair() {
        def httpClient = new HTTPClient(getContext())
        httpClient.getPredictions("1", "invalid")
        def errors = new JSONArray()
        def error = new JSONObject()

        try {
            error.put("error_code", "invalid_params_pair")
            errors.put(error)
            responseBody.put("errors", errors)
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
        setupMock(httpClient, 400, responseBody.toString())

        ret = httpClient.loadInBackground()

        assertEquals(400, Integer.parseInt(ret.get("statusCode").toString()))

        try {
            responseBody = new JSONObject(ret.get("body").toString())
            errors = responseBody.getJSONArray("errors")
            error = errors.getJSONObject(0)

            assertEquals("invalid_params_pair", error.getString("error_code"))
        } catch (JSONException e) {
            e.printStackTrace()
            fail()
        }
    }

    private void setupMock(def httpClient, def statusCode, final def responseBody) {
        try {
            when(con.getRequestMethod()).thenReturn("")
            when(con.getResponseCode()).thenReturn(statusCode)
            when(con.getInputStream()).thenReturn(new InputStream() {
                private int position = 0
                @Override
                int read() throws IOException {
                    return position < responseBody.length() ? responseBody.charAt(position++) : -1
                }

                @Override
                void close() throws IOException {}
            })

            Class<? extends HTTPClient> c = httpClient.getClass()
            def f = c.getDeclaredField("con")
            f.setAccessible(true)
            f.set(httpClient, con)
        } catch (NoSuchFieldException e) {
            e.printStackTrace()
        } catch (IllegalArgumentException e) {
            e.printStackTrace()
        } catch (IllegalAccessException e) {
            e.printStackTrace()
        } catch (IOException e) {
            e.printStackTrace()
        }
    }
}
