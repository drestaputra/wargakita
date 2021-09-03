package dresta.putra.wargakita.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import dresta.putra.wargakita.PrefManager;
import dresta.putra.wargakita.R;
import dresta.putra.wargakita.RetrofitClientInstance;
import dresta.putra.wargakita.peta.PaginationAdapterAset;
import dresta.putra.wargakita.peta.PetaPojo;
import dresta.putra.wargakita.peta.PetaResponsePojo;
import dresta.putra.wargakita.utils.PaginationScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class FragmentAset extends Fragment {
    private ProgressBar progressBar;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int PER_PAGE=20;
    private int currentPage = PAGE_START;
    private SearchView mSearchView;
    private String query_pencarian="";
    private PaginationAdapterAset adapter;
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private List<PetaPojo> results;
    private PrefManager prefManager;

    public FragmentAset() {
        // Required empty public constructor
    }
    interface APIAset{
        @FormUrlEncoded
        @POST("api/aset/data_aset")
        Call<PetaResponsePojo> getDataAset(
                @Field("pencarian") String pencarian,
                @Field("page") int page,
                @Field("perPage") int perPage
        );
    }
    private APIAset ServicePojo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_aset, container, false);
        prefManager = new PrefManager(getContext());

        results = new ArrayList<PetaPojo>();
        linearLayoutManager = new GridLayoutManager(getContext(),1);
        ServicePojo = RetrofitClientInstance.getRetrofitInstance(getContext()).create(APIAset.class);
        progressBar = view.findViewById(R.id.main_progress);
        mSearchView = view.findViewById(R.id.mSearchView);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_card_view);
        mShimmerViewContainer.startShimmerAnimation();
        RecyclerView rv = view.findViewById(R.id.RvData);
        adapter = new PaginationAdapterAset(getContext());
        adapter.clear();
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);
        loadFirstPage();

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                query_pencarian=query;
                if (!query.equals("")){
                    init_pencarian();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {

                mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
                query_pencarian=query;
                if (!query.equals("")){
                    init_pencarian();
                }
                return false;
            }
        });
        return view;
    }


    private void init_pencarian(){
        currentPage=0;
        isLastPage = false;
        PetaResponsePojoCall().enqueue(new Callback<PetaResponsePojo>() {
            @Override
            public void onResponse(Call<PetaResponsePojo> call, Response<PetaResponsePojo> response) {

                if (response.body() != null) {
                    if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        adapter.clear();
                        results = fetchResults(response);
                        adapter.addAll(results);
                        TOTAL_PAGES=response.body().getTotalPage();
                        if (currentPage <= TOTAL_PAGES-1) {
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                    }else{
                        adapter.clear();
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PetaResponsePojo> call, @NonNull Throwable t) {
               adapter.clear();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadFirstPage() {
        adapter.clear();
        PetaResponsePojoCall().enqueue(new Callback<PetaResponsePojo>() {
            @Override
            public void onResponse(Call<PetaResponsePojo> call, Response<PetaResponsePojo> response) {
                // Got data. Send it to adapter
                assert response.body() != null;
                if(response.body().getStatus()==200 && response.body().getTotalRecords()!=0) {
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);
                    TOTAL_PAGES=response.body().getTotalPage();
//
//
                    if (currentPage <= TOTAL_PAGES-1) {
                        adapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }else{
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Belum ada Data untuk saat ini", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PetaResponsePojo> call, Throwable t) {
                adapter.clear();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadNextPage() {
        PetaResponsePojoCall().enqueue(new Callback<PetaResponsePojo>() {
            @Override
            public void onResponse(Call<PetaResponsePojo> call, Response<PetaResponsePojo> response) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                adapter.removeLoadingFooter();
                isLoading = false;

                results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<PetaResponsePojo> call, Throwable t) {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Belum ada data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * @param response extracts List<{@link PetaPojo>} from response
     * @return null
     */
    private List<PetaPojo> fetchResults(Response<PetaResponsePojo> response) {
        PetaResponsePojo topRatedMovies = response.body();
        return topRatedMovies != null ? topRatedMovies.getData() : null;
    }
    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<PetaResponsePojo> PetaResponsePojoCall() {
        Call<PetaResponsePojo> petaResponsePojoCall = null;
        if (prefManager.getKontakPojo() != null && prefManager.getKontakPojo().getApp_is_aset_show()!=null && prefManager.getKontakPojo().getApp_is_aset_show().equals("0")){
            Toast.makeText(getContext(), "Maaf saat ini halaman data aset sedang dibatasi aksesnya.", Toast.LENGTH_SHORT).show();
            petaResponsePojoCall =  ServicePojo.getDataAset(
                    "",
                    0,
                    0
            );
        }else{
            petaResponsePojoCall = ServicePojo.getDataAset(
                    query_pencarian,
                    currentPage,
                    PER_PAGE
            );
        }
        return petaResponsePojoCall;

    }








}
