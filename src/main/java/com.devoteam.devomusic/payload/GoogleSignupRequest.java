package com.devoteam.devomusic.payload;

/**
 * A class for posting Google signup requests.
 */
public class GoogleSignupRequest {

    /**
     * The email of the Google user.
     */
    private String email;

    /**
     * The name of the Google user.
     */
    private String name;

    /**
     * The link to the users Google profile picture
     */
    private String providerPic;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderPic() {
        return providerPic;
    }

    public void setProviderPic(String providerPic) {
        this.providerPic = providerPic;
    }
}
