/**
 * 
 */
package com.bigberdranch.android.nerdlauncher;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author John
 * 
 */
public class NerdLauncherFragment extends ListFragment {
	private static final String TAG = "NerdLauncherFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent startupIntent = new Intent(Intent.ACTION_MAIN);
		startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final PackageManager pm = getActivity().getPackageManager();
		List<ResolveInfo> activies = pm.queryIntentActivities(startupIntent, 0);

		Log.i(TAG, "I've found " + activies.size() + " activies");

		Collections.sort(activies, new Comparator<ResolveInfo>() {
			@Override
			public int compare(ResolveInfo a, ResolveInfo b) {

				PackageManager pm = getActivity().getPackageManager();
				return String.CASE_INSENSITIVE_ORDER.compare(a.loadIcon(pm)
						.toString(), b.loadIcon(pm).toString());
			}

		});
		ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(
				getActivity(), android.R.layout.simple_list_item_1, activies) {
			public View getView(int pos, View convertView, ViewGroup parent) {
				View v = super.getView(pos, convertView, parent);
				TextView tv = (TextView) v;
				ResolveInfo ri = getItem(pos);
				tv.setText(ri.loadLabel(pm));
				return v;

			}
		};

		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick (ListView l, View v, int postion, long id){
		ResolveInfo resolveInfo = (ResolveInfo) l.getAdapter().getItem(postion);
		ActivityInfo activityInfo = resolveInfo.activityInfo;
		
		if (activityInfo == null)
			return;
	
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
		
		startActivity(i);
	}
	
}
