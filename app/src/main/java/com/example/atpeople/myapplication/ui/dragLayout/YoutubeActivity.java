package com.example.atpeople.myapplication.ui.dragLayout;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.atpeople.myapplication.R;
import com.example.atpeople.myapplication.customview.CustomVideoView;
import com.example.atpeople.myapplication.ui.dragLayout.view.YoutubeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Flavien Laurent (flavienlaurent.com) on 23/08/13.
 */
public class YoutubeActivity extends Activity {
	@BindView(R.id.dragLayout)
	YoutubeLayout youtubeLayout;
	@BindView(R.id.listView)
	ListView listView;
	@BindView(R.id.viewDesc)
	ListView viewDesc;
	@BindView(R.id.tv_item)
	TextView tv_item;
	@BindView(R.id.welcome_videoview)
	CustomVideoView welcome_videoview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youtobe_layout);
		ButterKnife.bind(this);
		welcome_videoview.setVideoURI(Uri.parse("android.resource://"+this.getPackageName()+"/"+R.raw.kr36));
		welcome_videoview.start();
		welcome_videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				welcome_videoview.start();

			}
		});
		init();
	}

	private void init() {
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				tv_item.setText(listView.getAdapter().getItem(i).toString());
				youtubeLayout.setVisibility(View.VISIBLE);
				youtubeLayout.maximize();
			}
		});

		listView.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return 50;
			}

			@Override
			public String getItem(int i) {
				return "youtobe" + i;
			}

			@Override
			public long getItemId(int i) {
				return i;
			}

			@Override
			public View getView(int i, View rView, ViewGroup viewGroup) {
				View view = rView;
				if (view == null) {
					view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, viewGroup, false);
				}
				((TextView) view.findViewById(android.R.id.text1)).setText(getItem(i));
				return view;
			}
		});

		viewDesc.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return 50;
			}

			@Override
			public String getItem(int position) {
				return "测试列表" + position;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = convertView;
				if (view == null) {
					view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
				}
				((TextView) view.findViewById(android.R.id.text1)).setText(getItem(position));
				return view;
			}
		});
	}

	@OnClick({})
	public void onViewClicked(View view) {
		switch (view.getId()){

		}
	}

}
