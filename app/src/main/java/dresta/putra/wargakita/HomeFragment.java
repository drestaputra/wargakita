package dresta.putra.wargakita;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Objects;

import dresta.putra.wargakita.agenda.AgendaActivity;
import dresta.putra.wargakita.laporan.LaporanActivity;
import dresta.putra.wargakita.slider.AdapterSlider;
import dresta.putra.wargakita.slider.SliderPojoResponse;
import dresta.putra.wargakita.surat.MenuSuratActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class HomeFragment extends Fragment {
    private ViewPager viewPager;
    private AdapterSlider adapter;
    private PrefManager prefManager;
    private ShimmerFrameLayout mShimmerViewContainerSlider;
    private GridLayoutManager linearLayoutManager;
    private LinearLayout LlNasabah,LlOperBerkas,LlInformasiProgram,LlSimulasi,LlPengaduan,LlJadwal,LlAngsur,LlTransaksi,LlLihatSetoran;
    private CardView CvRatioModal;
    private TextView TxvRatioModal, TxvRatioModalMsg;
    private ProgressBar PbRatioModal;
    private CardView CvBerita;

    public HomeFragment() {
        // Required empty public constructor
    }
    interface APIHomeFragment{
        @FormUrlEncoded
        @POST("api/slider/data_slider")
        Call<SliderPojoResponse> getSlider(@Field("page") int page, @Field("perPage") int perPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        APIHomeFragment apiHomeFragment = RetrofitClientInstance.getRetrofitInstance(getContext()).create(APIHomeFragment.class);
        Call<SliderPojoResponse> sliderPojoResponseCall = apiHomeFragment.getSlider(0,5);
        viewPager = view.findViewById(R.id.viewPager);
        prefManager = new PrefManager(getContext());
        TextView TxvUsername = view.findViewById(R.id.TxvUsername);
        TxvUsername.setText("Halo "+String.valueOf(prefManager.getUserPojo().getUsername()));
        TextView TxvNamaLengkap = view.findViewById(R.id.TxvNamaLengkap);
        TxvNamaLengkap.setText(String.valueOf(prefManager.getUserPojo().getNamaLengkap()));
        mShimmerViewContainerSlider = view.findViewById(R.id.shimmer_card_view_slider);
        mShimmerViewContainerSlider.startShimmerAnimation();
        mShimmerViewContainerSlider.setVisibility(View.VISIBLE);
        mShimmerViewContainerSlider.startShimmerAnimation();
        sliderPojoResponseCall.enqueue(new Callback<SliderPojoResponse>() {
            @Override
            public void onResponse(Call<SliderPojoResponse> call, final Response<SliderPojoResponse> response) {
                if (Objects.requireNonNull(response.body()).getStatus()==200){
                    adapter = new AdapterSlider(response.body().getData(), Objects.requireNonNull(getActivity()).getApplicationContext());
                    viewPager.setAdapter(adapter);
                    viewPager.setPadding(0, 0, 30, 0);
                }
                mShimmerViewContainerSlider.stopShimmerAnimation();
                mShimmerViewContainerSlider.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<SliderPojoResponse> call, Throwable t) {
                mShimmerViewContainerSlider.stopShimmerAnimation();
                mShimmerViewContainerSlider.setVisibility(View.GONE);
            }
        });
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
        CardView CvAgenda = view.findViewById(R.id.CvAgenda);
        CvAgenda.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), AgendaActivity.class);
            startActivity(intent);
        });
        CardView CvLaporan = view.findViewById(R.id.CvLaporan);
        CvLaporan.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), LaporanActivity.class);
            startActivity(intent);
        });
        CardView CvChat = view.findViewById(R.id.CvChat);
        CvChat.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
//            startActivity(intent);
        });
        CardView CvSurat = view.findViewById(R.id.CvSurat);
        CvSurat.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), MenuSuratActivity.class);
            startActivity(intent);
        });
        CardView CvKas = view.findViewById(R.id.CvKas);
        CvKas.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity().getApplicationContext(), AboutActivity.class);
                startActivity(intent);
        });
        CardView CvPengaduan = view.findViewById(R.id.CvPengaduan);
        CvPengaduan.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), CariActivity.class);
            startActivity(intent);
        });


        return view;
    }


}
