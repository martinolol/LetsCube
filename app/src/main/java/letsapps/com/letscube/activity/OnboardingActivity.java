package letsapps.com.letscube.activity;


import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import letsapps.com.letscube.R;
import letsapps.com.letscube.database.DatabaseSettingsHandler;
import letsapps.com.letscube.mixpanel.MixPanelHelper;
import letsapps.com.letscube.singleton.TimerSettings;
import letsapps.com.letscube.util.Theme;
import letsapps.com.letscube.util.TimerUtils;
import letsapps.com.letscube.util.User;
import letsapps.com.letscube.view.HoleImageView;
import letsapps.com.letscube.view.PageIndicatorView;
import letsapps.com.letscube.view.SpectreView;
import letsapps.com.letscube.view.chart.ChartLineView;
import letsapps.com.letscube.view.dialog.ViewDialogTitle;

public class OnboardingActivity extends Activity {

    public static final String IS_SHOWING_FINAL_PAGE = "IS_SHOWING_FINAL_PAGE";
    private boolean isShowingFinalPage;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static View mainLayout;
    private static HoleImageView holeIV;
    private ViewPager mViewPager;
    private static PageIndicatorView pageIndicatorView;
    private static TextView skipButton, nextButton;
    private static SpectreView step4Spectre = null;
    private static EditText otherEdit = null;

    private static final int NB_STEPS = 5;

    private static Theme onboardingTheme;
    private static boolean isThemeChange = false;
    private static String selectedAppDiscover;

    private Animation appearAnimation, disappearAnimation = null;

    private static DatabaseSettingsHandler settingsDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        if(getIntent().getExtras() != null){
            isShowingFinalPage = getIntent().getExtras().getBoolean(IS_SHOWING_FINAL_PAGE);
        }

        mainLayout = findViewById(R.id.main_layout);
        holeIV = (HoleImageView) findViewById(R.id.hole);
        mViewPager = (ViewPager) findViewById(R.id.container);
        pageIndicatorView = (PageIndicatorView) findViewById(R.id.page_indicator_view);
        skipButton = (TextView) findViewById(R.id.skip);
        nextButton = (TextView) findViewById(R.id.next);

        settingsDatabase = new DatabaseSettingsHandler(this);

        if(isShowingFinalPage){
            pageIndicatorView.setNbPages(NB_STEPS);
        }else{
            pageIndicatorView.setNbPages(NB_STEPS - 1);
        }
        pageIndicatorView.setCurrentPage(0);

        onboardingTheme = TimerSettings.getInstance().getTheme();

        mainLayout.setBackgroundColor(onboardingTheme.getMainColor());
        holeIV.setOutterColor(onboardingTheme.getMainColor());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        appearAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_appear);
        disappearAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_disappear);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setCurrentPage(position);

                if (isShowingFinalPage) {
                    if (position == NB_STEPS - 1) {
                        nextButton.setText(getString(R.string.ok));
                        nextButton.setOnClickListener(clickSkip);
                        skipButton.setVisibility(View.GONE);
                        holeIV.setVisibility(View.GONE);
                    } else {
                        nextButton.setText(getString(R.string.next));
                        nextButton.setOnClickListener(clickNextBeforeEnd);
                        skipButton.setVisibility(View.VISIBLE);
                        holeIV.setVisibility(View.VISIBLE);
                    }

                    if(position == NB_STEPS - 2){
                        nextButton.setEnabled(true);
                        nextButton.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                } else {
                    if (position == NB_STEPS - 2) {
                        nextButton.setText(getString(R.string.ok));
                        nextButton.setOnClickListener(clickSkip);
                        skipButton.setVisibility(View.GONE);
                    } else {
                        nextButton.setText(getString(R.string.next));
                        nextButton.setOnClickListener(clickNextBeforeEnd);
                        skipButton.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        nextButton.setOnClickListener(clickNextBeforeEnd);

        skipButton.setOnClickListener(clickSkip);
    }

    private View.OnClickListener clickNextBeforeEnd = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pageIndicatorView.setNextPage();
            mViewPager.setCurrentItem(pageIndicatorView.getCurrentPage());

        }
    };

    private View.OnClickListener clickSkip = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isShowingFinalPage){
                if(mViewPager.getCurrentItem() < NB_STEPS - 1){
                    mViewPager.setCurrentItem(NB_STEPS - 1, true);
                    return;
                }
                if(selectedAppDiscover == User.APP_DISCOVER_OTHER){
                    selectedAppDiscover = otherEdit.getText().toString();
                }
                MixPanelHelper.sendOnboardingInfo(
                        MixPanelHelper.getInstance(OnboardingActivity.this),
                        onboardingTheme.getName(OnboardingActivity.this),
                        selectedAppDiscover);
            }

            finish();
        }
    };

    public static class OnboardingFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static int currentStep;
        public OnboardingFragment() {
        }

        public static OnboardingFragment newInstance(int sectionNumber) {
            OnboardingFragment fragment = new OnboardingFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = null;

            currentStep = getArguments().getInt(ARG_SECTION_NUMBER);

            switch (currentStep){
                case 1 :
                    rootView = inflater.inflate(
                            R.layout.fragment_onboarding_step1_home, container, false);

                    break;
                case 2 :
                    rootView = inflater.inflate(
                            R.layout.fragment_onboarding_step2_record, container, false);
                    ViewDialogTitle dialogTitle =
                            (ViewDialogTitle)rootView.findViewById(R.id.record_dialog_title);
                    TextView messageTV = (TextView)rootView.findViewById(R.id.record_beat);
                    RelativeLayout recordBeatContent =
                            (RelativeLayout)rootView.findViewById(R.id.record_beat_content);
                    dialogTitle.setBackgroundColor(onboardingTheme.getMainColor());
                    messageTV.setText(getString(
                            R.string.beat_record_no_previous,
                            getString(R.string.event_3x3),
                            getString(R.string.onboarding_step2_record_single),
                            TimerUtils.formatTime(7346)));
                    recordBeatContent.setBackgroundColor(
                            onboardingTheme.getSecundaryColor());
                    break;
                case 3 :
                    rootView = inflater.inflate(
                            R.layout.fragment_onboarding_step3_analytics, container, false);
                    ChartLineView chartLV = (ChartLineView)
                            rootView.findViewById(R.id.onboarding_analytics_chartline);
                    chartLV.setIsOnboardingExemple(true);
                    break;
                case 4 :
                    rootView = inflater.inflate(
                            R.layout.fragment_onboarding_step4_theme, container, false);

                    step4Spectre = (SpectreView)rootView.findViewById(R.id.spectre);

                    break;
                case 5 :
                    rootView = inflater.inflate(
                            R.layout.fragment_onboarding_step5_access, container, false);
                    final SpectreView spectre = (SpectreView)rootView.findViewById(R.id.spectre);
                    final RadioGroup radios = (RadioGroup)rootView.findViewById(R.id.radios);
                    otherEdit =
                            (EditText)rootView.findViewById(R.id.radio_other_edit);

                    nextButton.setEnabled(false);
                    nextButton.setTextColor(Color.parseColor("#77FFFFFF"));

                    spectre.setCurrentTheme(onboardingTheme);
                    spectre.setSpectreListener(spectreListener);

                    radios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            nextButton.setEnabled(true);
                            nextButton.setTextColor(Color.parseColor("#FFFFFF"));

                            otherEdit.setVisibility(View.GONE);

                            switch (checkedId){
                                case R.id.radio_youtube :
                                    selectedAppDiscover = User.APP_DISCOVER_YOUTUBE;
                                    break;
                                case R.id.radio_forum :
                                    selectedAppDiscover = User.APP_DISCOVER_FORUM;
                                    break;
                                case R.id.radio_acquaintance :
                                    selectedAppDiscover = User.APP_DISCOVER_ACQUAINTANCE;
                                    break;
                                case R.id.radio_store :
                                    selectedAppDiscover = User.APP_DISCOVER_PLAYSTORE;
                                    break;
                                case R.id.radio_other :
                                    selectedAppDiscover = User.APP_DISCOVER_OTHER;
                                    otherEdit.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    });
                    break;
            }

            return rootView;
        }
    }

    private static SpectreView.SpectreListener spectreListener = new SpectreView.SpectreListener() {
        @Override
        public void OnSelectedThemeChange(Theme selectedTheme) {
            TimerSettings.getInstance().setTheme(selectedTheme);
            settingsDatabase.updateTheme(selectedTheme);
            onboardingTheme = selectedTheme;
            mainLayout.setBackgroundColor(onboardingTheme.getMainColor());
            holeIV.setOutterColor(onboardingTheme.getMainColor());
            isThemeChange = true;

            if(step4Spectre != null){
                step4Spectre.setCurrentTheme(selectedTheme);
            }
        }
    };

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a OnboardingFragment (defined as a static inner class below).
            return OnboardingFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            if(isShowingFinalPage){
                return NB_STEPS;
            }else{
                return NB_STEPS - 1;
            }
        }
    }

    @Override
    public void onBackPressed(){

        if(mViewPager.getCurrentItem() > 0){
            mViewPager.setCurrentItem(0, true);
        }else{
            if(isShowingFinalPage){
                MixPanelHelper.sendOnboardingInfo(
                        MixpanelAPI.getInstance(OnboardingActivity.this, MixPanelHelper.PROJECT_TOKEN),
                        "NULL", "NULL");
            }
            finish();
        }
    }
}
