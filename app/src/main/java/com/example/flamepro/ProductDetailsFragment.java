package com.example.flamepro;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProductDetailsFragment extends Fragment {

    private Product product;
    private int quantity = 1;
    private View flAnimOverlay;
    private ImageView ivAnimCart;

    public static ProductDetailsFragment newInstance(Product product) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        fragment.product = product;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (product == null) return;

        setupUI(view);
        setupQuantityPicker(view);
        setupCarousel(view);
        setupAnimations(view);
    }

    private void setupAnimations(View view) {
        flAnimOverlay = view.findViewById(R.id.flAnimOverlay);
        ivAnimCart = view.findViewById(R.id.ivAnimCart);
    }

    private void playAddToCartAnimation(View anchorView) {
        if (flAnimOverlay == null || ivAnimCart == null) return;

        int[] location = new int[2];
        anchorView.getLocationInWindow(location);
        
        ivAnimCart.setX(location[0] + (anchorView.getWidth() / 2f) - (ivAnimCart.getWidth() / 2f));
        ivAnimCart.setY(location[1] - (ivAnimCart.getHeight() / 2f));

        flAnimOverlay.setVisibility(View.VISIBLE);
        ivAnimCart.setAlpha(1.0f);
        ivAnimCart.setScaleX(0.5f);
        ivAnimCart.setScaleY(0.5f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new AccelerateInterpolator());

        ScaleAnimation scale = new ScaleAnimation(0.5f, 1.5f, 0.5f, 1.5f, 
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(500);

        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setStartOffset(300);
        fadeOut.setDuration(200);

        animationSet.addAnimation(scale);
        animationSet.addAnimation(fadeOut);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation) {
                flAnimOverlay.setVisibility(View.GONE);
            }
            @Override public void onAnimationRepeat(Animation animation) {}
        });

        ivAnimCart.startAnimation(animationSet);
    }

    private void setupUI(View view) {
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvCategory = view.findViewById(R.id.tvCategoryTag);
        TextView tvCurrentPrice = view.findViewById(R.id.tvCurrentPrice);
        TextView tvOriginalPrice = view.findViewById(R.id.tvOriginalPrice);
        TextView tvDiscount = view.findViewById(R.id.tvDiscount);
        TextView tvWeight = view.findViewById(R.id.tvWeight);
        TextView tvType = view.findViewById(R.id.tvType);
        TextView tvCoverage = view.findViewById(R.id.tvCoverage);
        ImageView ivBack = view.findViewById(R.id.ivBack);
        MaterialButton btnAddToCart = view.findViewById(R.id.btnAddToCart);
        MaterialButton btnBuyNow = view.findViewById(R.id.btnBuyNow);

        tvTitle.setText(product.getName());
        tvCategory.setText(product.getCategoryTag());
        
        tvCurrentPrice.setText(product.getPrice());
        tvOriginalPrice.setText(product.getOriginalPrice());
        tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvDiscount.setText(product.getDiscount());
        tvWeight.setText(product.getWeight());
        tvType.setText(product.getType());
        tvCoverage.setText(product.getCoverage());

        setupFeaturesList(view);

        ivBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addProduct(product, quantity);
            playAddToCartAnimation(v);
            Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
        });

        btnBuyNow.setOnClickListener(v -> {
            CartManager.getInstance().addProduct(product, quantity);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new CartFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setupFeaturesList(View view) {
        LinearLayout llFeaturesList = view.findViewById(R.id.llFeaturesList);
        llFeaturesList.removeAllViews();
        
        if (product.getKeyFeatures() != null) {
            for (String feature : product.getKeyFeatures()) {
                View featureView = LayoutInflater.from(getContext()).inflate(R.layout.item_feature, llFeaturesList, false);
                TextView tvFeature = featureView.findViewById(R.id.tvFeatureText);
                tvFeature.setText(feature);
                llFeaturesList.addView(featureView);
            }
        }
    }

    private void setupQuantityPicker(View view) {
        ImageView ivMinus = view.findViewById(R.id.ivMinus);
        ImageView ivPlus = view.findViewById(R.id.ivPlus);
        TextView tvQuantity = view.findViewById(R.id.tvQuantity);

        ivMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        ivPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });
    }

    private void setupCarousel(View view) {
        ViewPager2 vpCarousel = view.findViewById(R.id.vpProductImage);
        TabLayout tlDots = view.findViewById(R.id.tlDots);

        ImageCarouselAdapter adapter = new ImageCarouselAdapter(product.getCarouselImages());
        vpCarousel.setAdapter(adapter);

        new TabLayoutMediator(tlDots, vpCarousel, (tab, position) -> {}).attach();
    }
}
