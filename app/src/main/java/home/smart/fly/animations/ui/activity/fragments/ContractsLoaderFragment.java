package home.smart.fly.animations.ui.activity.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import home.smart.fly.animations.R;
import java8.util.Optional;

public class ContractsLoaderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private static final String TAG = "ContractsLoaderFragment";

    private List<String> datas = new ArrayList<>();
    private SearchView mSearchView;
    private String mCurFilter;
    private MyAdapter mMyAdapter;

    private final String CONTACTS = android.Manifest.permission.READ_CONTACTS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMyAdapter = new MyAdapter();
        recyclerView.setAdapter(mMyAdapter);

        setHasOptionsMenu(true);

        ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), o -> {
            if (o) {
                requestData();
            } else {
                Toast.makeText(getContext(), "Permission denied !", Toast.LENGTH_SHORT).show();
            }
        });

        Optional.ofNullable(getActivity()).ifPresent(fragmentActivity -> {
            if (PermissionChecker.checkSelfPermission(getActivity(), CONTACTS) == PermissionChecker.PERMISSION_GRANTED) {
                requestData();
            } else {
                launcher.launch(CONTACTS);
            }
        });

    }

    private void requestData() {
        LoaderManager.getInstance(this).initLoader(0, null, ContractsLoaderFragment.this);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Place an action bar item for searching.
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        mSearchView = new MySearchView(getActivity());
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        mSearchView.setIconifiedByDefault(true);
        item.setActionView(mSearchView);
    }

    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.CONTACT_STATUS, ContactsContract.Contacts.CONTACT_PRESENCE, ContactsContract.Contacts.PHOTO_ID, ContactsContract.Contacts.LOOKUP_KEY,};

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        Uri baseUri;
        if (mCurFilter != null) {
            baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(mCurFilter));
        } else {
            baseUri = ContactsContract.Contacts.CONTENT_URI;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND (" + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND (" + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
        Log.d(TAG, "baseUri : " + baseUri);
        Log.d(TAG, "proj : " + Arrays.toString(CONTACTS_SUMMARY_PROJECTION));
        Log.d(TAG, "select : " + select);

        return new CursorLoader(getContext(), baseUri, CONTACTS_SUMMARY_PROJECTION, select, null, ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        datas.clear();
        if (data != null) {
            data.moveToFirst();
            while (data.moveToNext()) {
                int index = data.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                if (index >= 0) {
                    String name = data.getString(index);
                    Log.e(TAG, "onLoadFinished: name==" + name);
                    datas.add(name);
                }
            }
        }

        mMyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        datas.clear();
        mMyAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onClose() {
        if (!TextUtils.isEmpty(mSearchView.getQuery())) {
            mSearchView.setQuery(null, true);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
        // Don't do anything if the filter hasn't actually changed.
        // Prevents restarting the loader when restoring state.
        if (mCurFilter == null && newFilter == null) {
            return true;
        }
        if (mCurFilter != null && mCurFilter.equals(newFilter)) {
            return true;
        }
        mCurFilter = newFilter;
        LoaderManager.getInstance(this).restartLoader(0, null, this);
        return true;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new MyHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyHolder holder, int position) {
            holder.mTextView.setText(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        static class MyHolder extends RecyclerView.ViewHolder {

            private final TextView mTextView;

            public MyHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.tv);
            }
        }

    }

    static class MySearchView extends SearchView {
        public MySearchView(Context context) {
            super(context);
        }

        // The normal SearchView doesn't clear its search text when
        // collapsed, so we will do this for it.
        @Override
        public void onActionViewCollapsed() {
            setQuery("", false);
            super.onActionViewCollapsed();
        }
    }
}
