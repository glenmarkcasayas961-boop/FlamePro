package com.example.flamepro;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProductDetailsFragment extends Fragment {

    private Product product;
    private int quantity = 1;

    public static ProductDetailsFragment newInstance(Product product) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable("product");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavigationVisibility(View.GONE);
        }

        if (product == null) return;

        setupUI(view);
        setupQuantityPicker(view);
        setupCarousel(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setBottomNavigationVisibility(View.VISIBLE);
        }
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
        TextView tvRatingScore = view.findViewById(R.id.tvRatingScore);
        TextView tvReviewCount = view.findViewById(R.id.tvReviewCount);
        TextView tvStockStatus = view.findViewById(R.id.tvStockStatus);
        ImageView ivBack = view.findViewById(R.id.ivBack);
        MaterialButton btnBuyNow = view.findViewById(R.id.btnBuyNow);
        MaterialButton btnAddToCart = view.findViewById(R.id.btnAddToCart);

        tvTitle.setText(product.getName());
        tvCategory.setText(product.getCategoryTag());
        
        tvCurrentPrice.setText(product.getPrice());
        tvOriginalPrice.setText(product.getOriginalPrice());
        tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvDiscount.setText(product.getDiscount());
        tvWeight.setText(product.getWeight());
        tvType.setText(product.getType());
        tvCoverage.setText(product.getCoverage());

        tvRatingScore.setText(String.valueOf(product.getRating()));
        tvReviewCount.setText("(" + product.getReviews() + " Reviews)");
        
        if (product.isInStock()) {
            tvStockStatus.setText(R.string.in_stock);
        } else {
            tvStockStatus.setText(R.string.out_of_stock);
        }
        tvStockStatus.setVisibility(View.VISIBLE);

        setupFeaturesList(view);

        ivBack.setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addProduct(product, quantity);
            performCartAnimation(view.findViewById(R.id.flCartAnim));
        });

        btnBuyNow.setOnClickListener(v -> {
            CartManager.getInstance().addProduct(product, quantity);
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new CartFragment())
                        .addToBackStack(null)
                        .commit();
            }
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

    private void performCartAnimation(View animView) {
        if (animView == null) return;
        
        animView.setVisibility(View.VISIBLE);
        animView.setScaleX(0f);
        animView.setScaleY(0f);
        animView.setAlpha(1f);

        animView.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(400)
                .setInterpolator(new OvershootInterpolator())
                .withEndAction(() -> {
                    animView.animate()
                            .scaleX(1.8f)
                            .scaleY(1.8f)
                            .alpha(0f)
                            .setDuration(500)
                            .setInterpolator(new AccelerateInterpolator())
                            .withEndAction(() -> animView.setVisibility(View.GONE))
                            .start();
                })
                .start();
    }
}
