package com.mobiwarez.laki.sville.configuration;

public interface ViewActionQueueProvider {

    ViewActionQueue queueFor(String queueId);

    void dispose(String queueId);
}
