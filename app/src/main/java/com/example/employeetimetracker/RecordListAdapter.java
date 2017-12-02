package com.example.employeetimetracker;

        import android.content.Context;
        import android.support.annotation.LayoutRes;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.TextView;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.List;


public class RecordListAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int mResource;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private ArrayList<String> records = new ArrayList<>();

    public RecordListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects, Context mContext) {
        super(context, resource, objects);
        this.mContext = mContext;
        this.mResource = resource;
        this.records = (ArrayList<String>) objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String record = getItem(position);
        LayoutInflater inflater;


        if(convertView == null) {
            inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }
        TextView eachRecordText = (TextView) convertView.findViewById(R.id.eachRecordText);
        Button xButton = (Button) convertView.findViewById(R.id.xButton);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference(user.getUid());

        eachRecordText.setText(record);

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oneRecord = records.get(position);

                if (oneRecord.endsWith("IN")) {
                    oneRecord = oneRecord.replaceAll(" : IN", "");
                } else if (oneRecord.endsWith("OUT")) {
                    oneRecord = oneRecord.replaceAll(" : OUT", "");
                }

                //records.remove(position);
                //System.out.println(records.size());
                //records.clear();

                mRef.child(oneRecord).removeValue();

            }
        });


        return convertView;
    }
}