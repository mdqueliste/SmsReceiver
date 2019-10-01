package ph.edu.uplb.ics.srg.smsreceiver;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsFragment extends Fragment{

    private Context context;
    private View view;

    public SettingsFragment(Context context){
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.settings, null);
        final EditText odettenum = (EditText) view.findViewById(R.id.odettenum_et);
        final EditText endpoint = (EditText) view.findViewById(R.id.endpoint_et);
        final EditText stakeholders = (EditText) view.findViewById(R.id.stakeholdernum_et);

        Button save = (Button) view.findViewById(R.id.saveSettings);
        odettenum.setText(MainActivity.getOdetteNum());  //setting the initial values
        endpoint.setText(MainActivity.getEndPoint());
        stakeholders.setText(MainActivity.getStakeholders());

        save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setOdetteNum(odettenum.getText().toString());  //changing the values to the EditText's values
                MainActivity.setEndPoint(endpoint.getText().toString());
                MainActivity.setStakeholders(stakeholders.getText().toString());
                Toast.makeText(context, "Settings successfully saved.", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}