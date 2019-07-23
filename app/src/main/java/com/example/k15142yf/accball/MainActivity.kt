package com.example.k15142yf.accball

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.util.Random

import android.util.Log
import android.view.SurfaceHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() ,SensorEventListener
        , SurfaceHolder.Callback {

    private var surfaceWidth: Int = 0
    private var surfaceHeight: Int = 0

    var rand = Random()
    var r =rand.nextInt(256)
    var g = rand.nextInt(256)
    var b =rand.nextInt(256)

    private val radius = 50.0f
    private  val coef = 1000.0f

    private var ballX: Float = 0f
    private var ballY: Float = 0f
    private var vx: Float = 0f
    private var vy: Float = 0f
    private var time: Long = 0L


    private fun drawCanvas( ){
        val canvas = surfaceView.holder.lockCanvas()
        canvas.drawColor(Color.YELLOW)
        canvas.drawCircle(ballX,ballY,radius, Paint().apply{
            color = Color.MAGENTA
        })

        surfaceView.holder.unlockCanvasAndPost(canvas)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override  fun onSensorChanged(event: SensorEvent?) {
        //val canvas = surfaceView.holder.lockCanvas()
        if (event == null) return

        if(time == 0L) time = System.currentTimeMillis()
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {

            val x = -event.values[0]
            val y = event.values[1]

            var t = (System.currentTimeMillis() - time).toFloat()
            time = System.currentTimeMillis()
            t /= 1000.0f

            val dx = vx * t + x * t * t / 2.0f
            val dy = vy * t + y * t * t / 2.0f
            ballX += dx * coef
            ballY += dy * coef
            vx += x * t
            vy += y * t

            if(ballX - radius < 0 && vx < 0){
                vx = -vx / 1.5f
                ballX = radius
               // canvas.drawRGB(r,g,b)
            }else if(ballX + radius > surfaceWidth && vx > 0){
                vx = -vx / 1.5f
                ballX = surfaceWidth - radius
               // canvas.drawRGB(r,g,b)
            }
            if(ballY - radius < 0 && vy < 0){
                vy = -vy / 1.5f
                ballY = radius
              //  canvas.drawRGB(r,g,b)
            }else if(ballY + radius > surfaceHeight && vy > 0){
                vy = -vy / 1.5f
                ballY = surfaceHeight - radius
               // canvas.drawRGB(r,g,b)
            }

            drawCanvas()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        val holder = surfaceView.holder
        holder.addCallback(this)
    }



    override fun onResume() {
        super.onResume()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE)
            as SensorManager
        val accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this,accSensor,SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE)
            as SensorManager
        sensorManager.unregisterListener(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE)
            as SensorManager
        val accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this,accSensor,SensorManager.SENSOR_DELAY_GAME)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        surfaceWidth = width
        surfaceHeight = height
        ballX = (width / 2).toFloat()
        ballY = (height / 2).toFloat()
    }



    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE)
            as SensorManager
        sensorManager.unregisterListener(this)
    }



}
