package junchi.flare;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity implements AdapterView.OnItemClickListener{
    ListView myList;
    MyDatabase db;
    SimpleCursorAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        myList = (ListView)findViewById(R.id.listView);
        db = new MyDatabase(this);

        // For the cursor adapter, specify which columns go into which views
        String[] fromColumns = {Constants.NAME, Constants.TYPE};
        int[] toViews = {R.id.plantNameEntry, R.id.plantTypeEntry }; // The TextView in simple_list_item_1

        Cursor cursor = db.getData();
        myAdapter = new SimpleCursorAdapter(this, R.layout.list_row, cursor, fromColumns, toViews, 2);
        myList.setAdapter(myAdapter);
        myList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout clickedRow = (LinearLayout) view;
        TextView plantNameTextView = (TextView) view.findViewById(R.id.plantNameEntry);
        TextView plantTypeTextView = (TextView) view.findViewById(R.id.plantTypeEntry);
        Toast.makeText(this, "row " + (1+position) + ":  " + plantNameTextView.getText() +" "+plantTypeTextView.getText(), Toast.LENGTH_LONG).show();
    }
}
