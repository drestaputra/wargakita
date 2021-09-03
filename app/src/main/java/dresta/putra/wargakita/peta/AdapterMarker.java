package dresta.putra.wargakita.peta;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.Objects;

import dresta.putra.wargakita.R;

public class AdapterMarker extends PagerAdapter {
    private ViewPager viewPager;
    private Button BtnDetail;
    private List<PetaPojo> models;
    private Context context;
    private AdapterFotoMarker adapterFotoMarker;

    public AdapterMarker(List<PetaPojo> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.adapter_marker, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        BtnDetail = view.findViewById(R.id.BtnDetail);
        ImageView imageView;
        TextView title, desc;

        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);

//        if (models.get(position).getImage_slider() != null && models.get(position).getImage_slider().length() > 0) {
//            Picasso.with(context).load(models.get(position).getImage_slider()).placeholder(R.color.greycustom2).into(imageView);
//        } else {
//            Picasso.with(context).load(R.color.greycustom2).into(imageView);
//        }
        if (models.get(position).getFoto_aset() != null) {
            adapterFotoMarker = new AdapterFotoMarker(models.get(position).getFoto_aset(), Objects.requireNonNull(context));
            viewPager.setAdapter(adapterFotoMarker);
        }
        title.setText(models.get(position).getNama_aset());
        desc.setText(Html.fromHtml(models.get(position).getKeterangan()));

        BtnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentl= new Intent(context.getApplicationContext(), DetailAsetActivity.class);
                intentl.putExtra("id_aset",models.get(position).getId_aset());
                intentl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentl);
            }
        });

        container.addView(view, 0);
        //        slider
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
