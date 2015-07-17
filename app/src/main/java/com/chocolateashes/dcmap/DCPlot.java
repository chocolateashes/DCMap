package com.chocolateashes.dcmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class DCPlot extends Activity implements Runnable
{
    public double AU_X = -77.090637;
    public double AU_Y = 38.936989;
    //AU is 38.936989,-77.090637
    public double AUgpsx = (1000 / Math.abs(DC_EAST - DC_WEST) * Math.abs(DC_WEST - AU_X)); //103.017; (1000/(DC_EAST-DC_WEST)*(DC_WEST-AU_X));
    public double AUgpsy = (1000 / Math.abs(DC_NORTH - DC_SOUTH) * Math.abs(DC_NORTH - AU_Y)); //287.155;
    // System.out.print  ((1000/Math.abs(DC_EAST-DC_WEST)*Math.abs(DC_WEST-AU_X))
    //public ArrayList<Point> coors;


    public double gpsx = (1000 / Math.abs(DC_EAST - DC_WEST) * Math.abs(DC_WEST - getLongitude())); //103.017; (1000/(DC_EAST-DC_WEST)*(DC_WEST-AU_X));
    public double gpsy = (1000 / Math.abs(DC_NORTH - DC_SOUTH) * Math.abs(DC_NORTH - getLatitude()));
    LinkedList coors = new LinkedList();


    class PlotView extends View {
        Bitmap dcmap;

        public PlotView(Context context) {
            super(context);
            dcmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dcmap), 1000, 1000, false);
        }

        public float scale = 0;

        // LinkedList coordinates = new LinkedList();
        protected void onDraw(Canvas g) {
            if (g.getWidth() / (float) (1000) < g.getHeight() / (float) (1000))
                scale = g.getWidth() / (float) (1000);
            else
                scale = g.getHeight() / (float) (1000);

            g.scale(scale, scale);

            Paint paint = new Paint();
            paint.setColor(Color.GREEN);

            g.drawBitmap(dcmap, 0, 0, null);
            // g.drawCircle((float)gpsx, (float)gpsy, 5, paint);

                for (int i = 0; i < coors.size() - 1; i++)
                {
                    double LON = (1000 / Math.abs(DC_EAST - DC_WEST) * Math.abs(DC_WEST - coors.getdataY(i)));
                    double LAT = (1000 / Math.abs(DC_NORTH - DC_SOUTH) * Math.abs(DC_NORTH - coors.getdataX(i)));
                    double LONnext = (1000 / Math.abs(DC_EAST - DC_WEST) * Math.abs(DC_WEST - coors.getdataY(i + 1)));
                    double LATnext = (1000 / Math.abs(DC_NORTH - DC_SOUTH) * Math.abs(DC_NORTH - coors.getdataX(i + 1)));
                    g.drawLine((float) LON, (float) LAT, (float) LONnext, (float) LATnext, paint);
                }



        }
    }
    PlotView pv;
    Thread pause;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeGPS();
        //coors = new ArrayList<Point>();
        pv = new PlotView(this);
        setContentView(pv);
        //coors.add(38.938708, -77.114395);
        //coors.add(38.934936, -77.112148);
        //coors.add(38.928393, -77.085025);
        pv.postInvalidate();
        pause = new Thread(this);
        pause.start();
    }
    @Override
    public void run()
    {
        Looper.prepare();
        while(true)
        {
            coors.add(getLatitude(), getLongitude());
            pv.postInvalidate();
            try {
                pause.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dcplot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    static final double DC_WEST=-77.119789, DC_EAST=-76.909399, DC_NORTH=38.99554, DC_SOUTH=38.79163;
    //AU is 38.936989,-77.090637
    private LocationManager lm;
    private void initializeGPS()
    {
        lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, new LocationListener(){
            public void onLocationChanged(Location location) {}
            public void onProviderDisabled(String provider) {}
            public void onProviderEnabled(String provider) {}
            public void onStatusChanged(String provider, int status, Bundle extras) {}});
    }

    private double getLatitude()
    {
        if (lm==null || lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)==null)
            return 0;
        return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
    }
    private double getLongitude()
    {
        if (lm==null || lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)==null)
            return 0;
        return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
    }

}

