package arindatiko.example.com.travelmecustomer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import arindatiko.example.com.travelmecustomer.R;
import arindatiko.example.com.travelmecustomer.model.Menu;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private Context context;
    private List<Menu> menus;

    public MenuAdapter(Context context, List<Menu> menus) {
        this.context = context;
        this.menus = menus;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Menu menu = menus.get(position);

        Glide.with(context)
                .load(menu.getFoto())
                .into(holder.imgItem);

        holder.tvTitle.setText(menu.getNama());
        holder.tvPrice.setText("Rp "+ menu.getHarga());
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgItem;
        private TextView tvTitle, tvPrice;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgItem = (ImageView) itemView.findViewById(R.id.img_item);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}

