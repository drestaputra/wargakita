package dresta.putra.wargakita.user;


import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dresta.putra.wargakita.R;
import dresta.putra.wargakita.chat.java.ui.activity.DialogsActivity;
import dresta.putra.wargakita.chat.java.utils.qb.QbUsersHolder;
import dresta.putra.wargakita.peta.DetailPetaActivity;

public class PaginationAdapterWarga extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<UserPojo> UserPojos;
    private Context context;

    private boolean isLoadingAdded = false;
    public PaginationAdapterWarga(Context context) {
        this.context = context;
        UserPojos = new ArrayList<>();
    }


    public void setUserPojos(List<UserPojo> UserPojos) {
        this.UserPojos = UserPojos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, @NonNull LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.adapter_warga, parent, false);
        viewHolder = new UserPojoVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final UserPojo result = UserPojos.get(position); // UserPojo
        switch (getItemViewType(position)) {
            case ITEM:
                final UserPojoVH Vh = (UserPojoVH) holder;
                Vh.TxvNamaUser.setText(result.getNamaLengkap());
                Vh.TxvKeluarga.setText("Keluarga X"+ String.valueOf(Math.random()));
                if (result.getFoto_user() != null && result.getFoto_user().length() > 0) {
                    Picasso.with(context).load(result.getFoto_user()).placeholder(R.color.greycustom2).into(Vh.IvFotoUser);
                } else {
                    Picasso.with(context).load(R.color.greycustom2).into(Vh.IvFotoUser);
                }
                Vh.BtnDetail.setOnClickListener(v -> {
//                        Intent Idetail = new Intent(context.getApplicationContext(), DetailAsetActivity.class);
//                        Idetail.putExtra("id_aset",result.getId_aset());
//                        Idetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(Idetail);
                });
                if (result.getLatitude().isEmpty() || result.getLatitude().equals("0") || result.getLongitude().isEmpty() || result.getLongitude().equals("0")){
                    Vh.BtnMap.setVisibility(View.INVISIBLE);
                }
                Vh.BtnMap.setOnClickListener(v -> {
                        Intent Idetail = new Intent(context.getApplicationContext(), DetailPetaActivity.class);
                        Idetail.putExtra("id_user",result.getId_user());
                        Idetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(Idetail);
                });
                Vh.BtnChat.setOnClickListener(v -> {
                    DialogsActivity.start(context);
                });


                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return UserPojos == null ? 0 : UserPojos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == UserPojos.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(UserPojo r) {
        UserPojos.add(r);
        notifyItemInserted(UserPojos.size() - 1);
    }

    public void addAll(@NonNull List<UserPojo> moveResults) {
        for (UserPojo result : moveResults) {
            add(result);
        }
    }

    public void remove(UserPojo r) {
        int position = UserPojos.indexOf(r);
        if (position > -1) {
            UserPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new UserPojo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = UserPojos.size() - 1;
        UserPojo result = getItem(position);

        if (result != null) {
            UserPojos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public UserPojo getItem(int position) {
        return UserPojos.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class UserPojoVH extends RecyclerView.ViewHolder {
        private ImageView IvFotoUser;
        private Button BtnDetail;
        private ImageView imageView,BtnMap, BtnChat;
        private TextView TxvNamaUser, TxvKeluarga;

        public UserPojoVH(@NonNull View itemView) {
            super(itemView);
            IvFotoUser = itemView.findViewById(R.id.IvFotoUser);
            BtnDetail = itemView.findViewById(R.id.BtnDetail);
            BtnChat = itemView.findViewById(R.id.BtnChat);
            BtnMap = itemView.findViewById(R.id.BtnMap);
            imageView = itemView.findViewById(R.id.imageView);
            TxvNamaUser = itemView.findViewById(R.id.TxvNamaUser);
            TxvKeluarga = itemView.findViewById(R.id.TxvKeluarga);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
        }
    }


}


