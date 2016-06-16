package br.edu.unicatolica.meuslivros;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by josimarjunior on 14/06/16.
 */
public class LoginFragment extends Fragment {

    private CallbackManager callbackManager = null;
    private AccessTokenTracker accessTokenTracker = null;
    private ProfileTracker profileTracker = null;

    public static final String PARCEL_KEY = "parcel_key";

    private LoginButton loginButton;

    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            homeFragment(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.v("AccessTokenTracker", "oldAccessToken=" + oldAccessToken + "||" + "CurrentAccessToken" + currentAccessToken);
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.v("Session Tracker", "oldProfile=" + oldProfile + "||" + "CurrentProfile" + currentProfile);
                homeFragment(currentProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    private void homeFragment(Profile profile) {

        if(profile != null) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(PARCEL_KEY, profile);
            HomeFragment hf = new HomeFragment();
            hf.setArguments(bundle);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainContainer, new HomeFragment());
            fragmentTransaction.commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.login_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        loginButton = (LoginButton) view.findViewById(R.id.login_button);

        loginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }


    public boolean isLoggedIn() {
        AccessToken aceAccessToken = AccessToken.getCurrentAccessToken();
        return aceAccessToken != null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isLoggedIn()) {
            loginButton.setVisibility(View.INVISIBLE);
            Profile profile = Profile.getCurrentProfile();
            homeFragment(profile);
        }
    }
}
