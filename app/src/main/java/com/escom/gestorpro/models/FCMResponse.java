package com.escom.gestorpro.models;

import java.util.ArrayList;

public class FCMResponse {
    private long multicast_id;
    private int success;
    private int failure;
    private int cannonical_ids;
    ArrayList<Object> results = new ArrayList<Object>();

    public FCMResponse(long multicast_id, int success, int failure, int cannonical_ids, ArrayList<Object> results) {
        this.multicast_id = multicast_id;
        this.success = success;
        this.failure = failure;
        this.cannonical_ids = cannonical_ids;
        this.results = results;
    }

    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCannonical_ids() {
        return cannonical_ids;
    }

    public void setCannonical_ids(int cannonical_ids) {
        this.cannonical_ids = cannonical_ids;
    }

    public ArrayList<Object> getResults() {
        return results;
    }

    public void setResults(ArrayList<Object> results) {
        this.results = results;
    }
}
