package com.my.samplemusicplayertest.ui.adapters.viewholders;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.my.samplemusicplayertest.glide.audiocover.AudioFileCover;
import com.my.samplemusicplayertest.utils.ViewsUtil;
import com.my.samplemusicplayertest.utils.tasks.ITask;
import com.my.samplemusicplayertest.utils.tasks.UITask;
import com.my.samplemusicplayertest.utils.tasks.UITaskExecute;
import com.my.samplemusicplayertest.R;
import com.my.samplemusicplayertest.ui.adapters.BaseRecyclerViewAdapter;
import com.my.samplemusicplayertest.ui.adapters.helpers.CacheHelper;
import com.my.samplemusicplayertest.ui.adapters.models.BaseRecyclerViewItem;
import com.my.samplemusicplayertest.ui.adapters.models.SongRecyclerViewItem;

public class SongViewHolder extends BaseViewHolder {

    private SongRecyclerViewItem m_vItem;

    private ConstraintLayout m_vRootView;

    private CardView m_vImageView_Art_Parent;
    private ConstraintLayout m_vLayout_Description;
    private TextView m_vTextView_Title;
    private ConstraintLayout.LayoutParams m_vTextView_Title_Params;
    private TextView m_vTextView_Artist;
    private ConstraintLayout.LayoutParams m_vTextView_Artist_Params;

    private ImageView m_vImageView_Art;

    private Runnable m_vImageLoader;

    public SongViewHolder(@NonNull View itemView) {
        super(itemView);

        this.m_vRootView = (ConstraintLayout) itemView;

        this.m_vLayout_Description   = findViewById(R.id.item_song_description_parent);
        this.m_vImageView_Art_Parent = findViewById(R.id.item_song_art_image_view_parent);

        this.m_vImageLoader = this::onLoadImage;
    }

    @Override
    public void onInitializeView(BaseRecyclerViewAdapter.ViewType viewType) {
        if (viewType == this.m_vViewType)
            return;

        int paddingSize = ViewsUtil.dp2px(this.itemView.getContext(), R.dimen.item_library_song_padding);
        ConstraintLayout parentLayout = (ConstraintLayout) findViewById(R.id.item_root_view);

        ConstraintLayout.LayoutParams imgLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        imgLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        imgLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        imgLayoutParams.dimensionRatio = "1:1";

        ConstraintLayout.LayoutParams descLayoutParams = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        descLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        descLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;

        switch (viewType) {
            case GRID:
                Size def_img_size = CacheHelper.getInstance().getSize(CacheHelper.IMAGE_SIZE.MEDIUM);

                this.m_vRootView.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
                this.m_vLayout_Description.setPadding(0, (int)(paddingSize * 0.75F), 0, 0);

                imgLayoutParams.width = def_img_size.getWidth();
                imgLayoutParams.height = def_img_size.getHeight();

                imgLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
                imgLayoutParams.bottomToTop = R.id.item_song_description_parent;

                descLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                descLayoutParams.topToBottom = m_vImageView_Art_Parent.getId();

                this.m_vImageView_Art_Parent.setLayoutParams(new LinearLayout.LayoutParams(def_img_size.getWidth(), def_img_size.getHeight()));

                if (this.isViewsCreated()) {
                    this.m_vImageView_Art.setMinimumHeight(def_img_size.getHeight());
                    this.m_vImageView_Art.setMinimumWidth(def_img_size.getWidth());
                }

                parentLayout.updateViewLayout(m_vImageView_Art_Parent, imgLayoutParams);
                parentLayout.updateViewLayout(m_vLayout_Description, descLayoutParams);

                break;

            case LIST:
                this.m_vRootView.setPadding(paddingSize, paddingSize, paddingSize, 0);
                this.m_vLayout_Description.setPadding(paddingSize, 0, 0, 0);
                imgLayoutParams.endToStart = R.id.item_song_description_parent;
                imgLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

                descLayoutParams.startToEnd = m_vImageView_Art_Parent.getId();
                descLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

                int img_size = ViewsUtil.dp2px(itemView.getContext(), R.dimen.item_library_song_art_size);

                imgLayoutParams.width = img_size;
                imgLayoutParams.height = img_size;

                if (this.isViewsCreated()) {
                    this.m_vImageView_Art.setMinimumHeight(img_size);
                    this.m_vImageView_Art.setMinimumWidth(img_size);
                }

                parentLayout.updateViewLayout(m_vImageView_Art_Parent, imgLayoutParams);
                parentLayout.updateViewLayout(m_vLayout_Description, descLayoutParams);

                break;
        }

        this.m_vViewType = viewType;
    }

    @Override
    public void onCreateViews() {
        UITaskExecute.execute(((ITask) () -> {
            // Create Image View
            this.m_vImageView_Art = new ImageView(itemView.getContext());
            this.m_vImageView_Art.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Create Text View Title
            this.m_vTextView_Title = new TextView(itemView.getContext());
            this.m_vTextView_Title.setId(R.id.text_view_song_title);
            this.m_vTextView_Title.setMaxLines(1);
            this.m_vTextView_Title.setText(this.m_vItem.getTitle());
            this.m_vTextView_Title.setTextSize(this.itemView.getResources().getDimensionPixelSize(R.dimen.font_size_md));
            this.m_vTextView_Title.setTypeface(null, Typeface.BOLD);

            m_vTextView_Title_Params = new ConstraintLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            m_vTextView_Title_Params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            m_vTextView_Title_Params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

            // Create Text View Artist
            this.m_vTextView_Artist = new TextView(itemView.getContext());
            this.m_vTextView_Artist.setMaxLines(1);
            this.m_vTextView_Artist.setText(this.m_vItem.getSong().getArtistName());
            this.m_vTextView_Artist.setTextSize(this.itemView.getResources().getDimensionPixelSize(R.dimen.font_size_sm));

            m_vTextView_Artist_Params = new ConstraintLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            m_vTextView_Artist_Params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            m_vTextView_Artist_Params.topToBottom = R.id.text_view_song_title;

            super.onCreateViews(); // Do not delete
        }).build(15, true, 10009 + (int) getItemId(), UITask.Priority.HIGH, true));

        UITaskExecute.execute(((ITask) () -> {
            ConstraintLayout layout = m_vLayout_Description.findViewById(R.id.placeholder_view);
            m_vLayout_Description.removeView(layout);

            this.m_vImageView_Art_Parent.addView(this.m_vImageView_Art, MATCH_PARENT, MATCH_PARENT);
            this.m_vLayout_Description.addView(this.m_vTextView_Title, this.m_vTextView_Title_Params);
            this.m_vLayout_Description.addView(this.m_vTextView_Artist, this.m_vTextView_Artist_Params);
        }).build(15, true, 10001 + (int) getItemId(), UITask.Priority.NORMAL));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewItem viewItem) {
        if (viewItem != null)
            m_vItem = (SongRecyclerViewItem) viewItem;

        if (this.isViewsCreated()) {
            this.m_vTextView_Title.setText(this.m_vItem.getTitle());
            this.m_vTextView_Artist.setText(this.m_vItem.getSong().getArtistName());

            this.m_vImageView_Art.setImageResource(com.realgear.icons_pack.R.drawable.ic_album_24px);
        }

        this.onReloadData();
    }

    @Override
    public void onReloadData() {
        UITaskExecute.execute(((ITask)() -> this.m_vImageLoader.run()).build(5, false, 10004 + (int) getItemId(), UITask.Priority.LOW));
    }

    public void onLoadImage() {
        Size def_img_size = CacheHelper.getInstance().getSize((m_vViewType == BaseRecyclerViewAdapter.ViewType.GRID) ? CacheHelper.IMAGE_SIZE.MEDIUM : CacheHelper.IMAGE_SIZE.SMALL);

        Glide.with(itemView.getContext())
                .load(new AudioFileCover(m_vItem.getFilePath()))
                .set(Downsampler.DECODE_FORMAT, DecodeFormat.PREFER_RGB_565)
                .onlyRetrieveFromCache(true)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(com.realgear.icons_pack.R.drawable.ic_album_24px)
                .centerCrop()
                .dontAnimate()
                .apply(new RequestOptions()).override(def_img_size.getWidth(), def_img_size.getHeight())
                .signature(new ObjectKey(m_vItem.getId()))
                .priority(Priority.IMMEDIATE)
                .listener(new RequestListener<>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        m_vShimmerEffect.stopShimmer();
                        m_vShimmerEffect.hideShimmer();
                        UITaskExecute.getInstance().onTaskComplete();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        m_vShimmerEffect.stopShimmer();
                        m_vShimmerEffect.hideShimmer();
                        UITaskExecute.getInstance().onTaskComplete();
                        return false;
                    }
                })
                .into(m_vImageView_Art);
    }
}
