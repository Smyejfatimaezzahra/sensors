package ensa.ma.sensors.ui.SansAcc;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Date;

import ensa.ma.sensors.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GravityRotation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GravityRotation extends Fragment implements SensorEventListener, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SensorManager mSensorManager;
    private Sensor mGravitySensor;
    private Sensor mRotationSensor;
    private TextView Gx;
    private TextView Gy;
    private TextView Gz;
    private TextView Ax;
    private TextView Ay;
    private TextView Az;
    private RadioButton X;
    private RadioButton Y;
    private RadioButton Z;
    private  static int value;
    private LineChart chart;
    static ArrayList<Entry> entriesG = new ArrayList<>();
    static ArrayList<Entry> entriesR = new ArrayList<>();
    public GravityRotation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GravityRotation.
     */
    // TODO: Rename and change types and number of parameters
    public static GravityRotation newInstance(String param1, String param2) {
        GravityRotation fragment = new GravityRotation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if( mGravitySensor == null){
            Toast.makeText(getContext(), R.string.message_neg, Toast.LENGTH_LONG).show();
        }
        if( mRotationSensor == null){
            Toast.makeText(getContext(), R.string.message_neg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_gravity_rotation, container, false);
        Gx=root.findViewById(R.id.Gx);
        Gy=root.findViewById(R.id.Gy);
        Gz=root.findViewById(R.id.Gz);
        Ax=root.findViewById(R.id.Ax);
        Ay=root.findViewById(R.id.Ay);
        Az=root.findViewById(R.id.Az);
       chart=root.findViewById(R.id.chart);
        X=root.findViewById(R.id.x);
        Y=root.findViewById(R.id.y);
        Z=root.findViewById(R.id.z);
        X.setOnClickListener(this);
        Y.setOnClickListener(this);
        Z.setOnClickListener(this);
        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mRotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        entriesG.clear();
        entriesR.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        entriesG.clear();
        entriesR.clear();
    }
    @Override
    public void onSensorChanged(SensorEvent e) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            if(e.sensor.getStringType().equals("android.sensor.gravity")){
                Log.d("gravity","true");
                Gx.setText(e.values[0]+"");
                Gy.setText(e.values[1]+"");
                Gz.setText(e.values[2]+"");
            }
            else{
                Log.d("rotaion",e.sensor.getStringType());
                Ax.setText(e.values[0]+"");
                Ay.setText(e.values[1]+"");
                Az.setText(e.values[2]+"");
            }
        }
        addEntry(e);



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onClick(View view) {
        if (view==X)  value=0;
        else if (view==Y) value=1;
        else value=2;
        entriesG.clear();
        entriesR.clear();

    }
    private void addEntry(SensorEvent event) {
        Date d = new Date();
        if(event.sensor.getType()==Sensor.TYPE_GRAVITY){
            entriesG.add(new Entry(entriesG.size(), event.values[value]));
        }
       else entriesR.add(new Entry(entriesR.size(), event.values[value]));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet dataSet = new LineDataSet(entriesG, "Gravity - Time series");
        LineDataSet dataSetR = new LineDataSet(entriesR, "  Rotation - Time series");
        dataSetR.setColor(Color.RED);
        dataSetR.setCircleColor(Color.RED); //Line color
        dataSetR.setFillColor(Color.RED);
        dataSets.add(dataSet);
        dataSets.add(dataSetR);
        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);
        chart.notifyDataSetChanged();
        //refresh
        chart.invalidate();
    }
}