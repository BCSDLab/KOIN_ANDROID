package in.koreatech.koin.ui.event.adapter;

public class EventRecyclerAdapter extends RecyclerView.Adapter<AdvertisingRecyclerAdapter.ViewHolder> {

    private ArrayList<Advertising> adArrayList;
    private Context context;
    private final RequestOptions glideOptions;

    public AdvertisingRecyclerAdapter(ArrayList<Advertising> adArrayList, Context context) {
        this.adArrayList = adArrayList;
        this.context = context;

        glideOptions = new RequestOptions()
                .fitCenter()
                .override(158, 106)
                .error(R.drawable.img_noimage)
                .placeholder(R.color.white);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view; //onBindViewHolder 함수에서 아이템의 클릭리스너를 달기위한 view
        private Context context;
        @BindView(R.id.advertising_recyclerview_item_food_imageview)
        ImageView adFoodImageview;
        @BindView(R.id.advertising_recyclerview_item_store_title_textview)
        TextView adTitleTextview;
        @BindView(R.id.advertising_recyclerview_item_event_contents_textview)
        TextView adContentsTextview;
        @BindView(R.id.advertising_recyclerview_item_period_textview)
        TextView adPeriodTextview;
        @BindView(R.id.advertising_recyclerview_item_publish_date_textview)
        TextView adPublishedDateTextview;
        @BindView(R.id.adviertising_recyclerview_margam_imageview)
        ImageView adMargamImageview;
        @BindView(R.id.advertising_recyclerview_item_margam_textview)
        TextView adMargamTextview;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        void onBind(Advertising ad) {

            if (ad.getEventTitle() == null)
                adTitleTextview.setText("-");
            else
                adTitleTextview.setText(ad.getTitle());

            if (ad.getContent() == null)
                adContentsTextview.setText("-");
            else
                adContentsTextview.setText(ad.getEventTitle());

            if (ad.getStartDate() == null && ad.getEndDate() == null)
                adPeriodTextview.setText("-");
            else
                adPeriodTextview.setText(ad.startDate + "~" + ad.getEndDate());

            String[] publishedDate = ad.getPublishedDate().split(" ");

            if (ad.getPublishedDate() == null)
                adPublishedDateTextview.setText("-");
            else
                adPublishedDateTextview.setText(publishedDate[0]);

            Date date = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if ((date.compareTo(formatDate.parse(ad.endDate))  == 1)){
                    adMargamImageview.setVisibility(View.VISIBLE);
                    adMargamTextview.setVisibility(View.VISIBLE);
                    adMargamImageview.setColorFilter(Color.parseColor("#8C8C8C"), PorterDuff.Mode.LIGHTEN);
                    adFoodImageview.setColorFilter(Color.parseColor("#8C8C8C"), PorterDuff.Mode.LIGHTEN);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public AdvertisingRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertising_recyclerview_item, parent, false);
        return new AdvertisingRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertisingRecyclerAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.onBind(adArrayList.get(position));
        Advertising ad = adArrayList.get(position);

        Glide.with(context)
                .load(ad.thumbnail)
                .apply(glideOptions)
                .into(holder.adFoodImageview);
    }

    @Override
    public int getItemCount() {
        return adArrayList.size();
    }


    public void setAdArrayList(ArrayList<Advertising> adArrayList) {
        this.adArrayList = adArrayList;
    }
}
