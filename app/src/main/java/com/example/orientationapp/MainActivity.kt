package com.example.orientationapp

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import com.android.volley.VolleyError
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.RequestQueue




class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) //locks in portrait mode
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val vectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener (
        this,
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
        SensorManager.SENSOR_DELAY_NORMAL
    )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


    override fun onSensorChanged(event: SensorEvent?) {
        var rotationMatrix: FloatArray =
            floatArrayOf(0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f)
        var orientation: FloatArray = floatArrayOf(0.0f,0.0f,0.0f)

        SensorManager.getRotationMatrixFromVector(rotationMatrix, event!!.values)
        SensorManager.getOrientation(rotationMatrix, orientation)

        Log.d("raw data", "${event.values[0]}, ${event.values[1]}, ${event.values[2]}")
        Log.d("", "1 ${orientation[0]} ${orientation[1]} ${orientation[2]}")
        var azimuth = orientation[0]
        var pitch = orientation[1]
        var roll = orientation[2]
        textView.text = "Yaw: ${azimuth}\n\nPitch: ${pitch}\n\nRoll: ${roll}"
        url = "https://www.eeng.dcu.ie/~sadleirr/sensorlog/sensorlog.php?pitch=${pitch}&roll=${roll}&yaw=${azimuth}"
        makeHttpRequest(url)

    }

    private fun makeHttpRequest(url : String){
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                if (null != response) {
                    try {

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }, Response.ErrorListener { })
        queue.add(request)
    }

}










/*
        val connection = url.openConnection() as
                HttpURLConnection
        try {
            Log.d("Connection", "Attempting Connection")


            val input = connection.inputStream
            val reader = InputStreamReader(input)
            Log.d("Connection", "got input Connection")


            var data = reader.read()
            while(data != -1){
                val current: Char = data as Char
                data = reader.read()
                Log.d("CurrentData", "${current}")
            }
        }catch (e: Exception){
            e.printStackTrace()
            Log.e("CatchTriggered", "${e}")

        }
        finally {
            connection.disconnect()
        }
        */