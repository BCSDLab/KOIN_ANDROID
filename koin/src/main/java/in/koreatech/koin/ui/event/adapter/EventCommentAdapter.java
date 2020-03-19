package in.koreatech.koin.ui.event.adapter;

public class EventCommentAdapter extends RecyclerView.Adapter<AdvertisingCommentAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Comment> adDetailComment;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nicknameTextview;
        TextView timeTextview;
        TextView contentsTextview;
        Button fixButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nicknameTextview = (TextView)itemView.findViewById(R.id.advertising_comment_item_nickname_textview);
            timeTextview = (TextView) itemView.findViewById(R.id.advertising_comment_item_time_textview);
            contentsTextview = (TextView) itemView.findViewById(R.id.advertising_comment_item_contents_textview);
            fixButton = (Button)itemView.findViewById(R.id.advertising_comment_item_fix_button);
        }
    }

    public AdvertisingCommentAdapter(Context context, ArrayList<Comment> adComment) {
        this.context = context;
        this.adDetailComment = adComment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertising_detail_comment_recyclerview_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nicknameTextview.setText(adDetailComment.get(position).authorNickname);
        holder.timeTextview.setText(adDetailComment.get(position).createDate);
        holder.contentsTextview.setText(adDetailComment.get(position).content);
//       holder.fitButton.setOnClickListener(i->{
//           //수정버튼 반영하기
//       });

    }

    @Override
    public int getItemCount() {
        return adDetailComment.size();
    }
}
