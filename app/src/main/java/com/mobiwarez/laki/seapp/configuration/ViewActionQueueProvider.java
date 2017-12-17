package com.mobiwarez.laki.seapp.configuration;

public interface ViewActionQueueProvider {

    ViewActionQueue queueFor(String queueId);

    void dispose(String queueId);
}
