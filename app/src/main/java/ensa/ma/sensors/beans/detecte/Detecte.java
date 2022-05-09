package ensa.ma.sensors.beans.detecte;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ensa.ma.sensors.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Detecte#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Detecte extends Fragment implements SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView detecte;
    private SensorManager mSensorManager;
    private Sensor mAccelarationSensor;

    public Detecte() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Detecte.
     */
    // TODO: Rename and change types and number of parameters
    public static Detecte newInstance(String param1, String param2) {
        Detecte fragment = new Detecte();
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
        mAccelarationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if( mAccelarationSensor == null){
            Toast.makeText(getContext(), R.string.message_neg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  root=inflater.inflate(R.layout.fragment_detecte, container, false);
        detecte=root.findViewById(R.id.detecte);
        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelarationSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("Accelration", String.valueOf(sensorEvent.values[0]));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public  void detecte(SensorEvent e){
        if(e.values[0]>0 ) detecte.setText("entrin de marché");
        if(e.values[1]>0 && e.values[0]==0.0 ) detecte.setText("debout");
        if(e.values[1]<0 && e.values[0]==0.0 ) detecte.setText("assis");
        if(e.values[1]<0 && e.values[0]==0.0 ) detecte.setText("assis");

    }
}