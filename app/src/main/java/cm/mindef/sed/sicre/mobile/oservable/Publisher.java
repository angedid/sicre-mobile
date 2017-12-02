package cm.mindef.sed.sicre.mobile.oservable;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cm.mindef.sed.sicre.mobile.oservable.event.RessourceCreated;

/**
 * Created by nkalla on 03/11/17.
 */


public class Publisher {

    private static final ThreadLocal<Publisher> instance = new ThreadLocal<Publisher>() {
        protected Publisher initialValue() {
            return new Publisher();
        }
    };

    private boolean publishing;

    private List subscribers;

    private Publisher() {
        super();
        this.setPublishing(false);
        this.ensureSubscribersList();
    }

    public static Publisher instance() {
        return instance.get();
    }

    public <T> void publish(final T aDomainEvent) {
        if (!this.isPublishing() && this.hasSubscribers()) {
            try {
                this.setPublishing(true);
                Class<?> eventType = aDomainEvent.getClass();
                List<DomainEventSubscriber<T>> allSubscribers = this.subscribers();

                for (DomainEventSubscriber<T> subscriber : allSubscribers) {
                    subscriber.handleEvent(aDomainEvent);
                    /*Class<?> subscribedToType = subscriber.subscribedToEventType();
                    if (eventType == subscribedToType || subscribedToType == DomainEvent.class) {
                        subscriber.handleEvent(aDomainEvent);
                    }*/
                }

            } finally {
                this.setPublishing(false);
            }
        }
    }

    public void save() {
        if (!this.isPublishing() && this.hasSubscribers()) {
            try {
                this.setPublishing(true);

                List<MyDomainEventSubscriber> allSubscribers = this.subscribers();

                for (MyDomainEventSubscriber subscriber : allSubscribers) {
                    synchronized (subscriber){
                        subscriber.handleEvent();
                    }

                    /*Class<?> subscribedToType = subscriber.subscribedToEventType();
                    if (eventType == subscribedToType || subscribedToType == DomainEvent.class) {
                        subscriber.handleEvent(aDomainEvent);
                    }*/
                }

            } finally {
                this.setPublishing(false);
            }
        }
    }

    public void publishAll(Collection<DomainEvent> aDomainEvents) {
        for (DomainEvent domainEvent : aDomainEvents) {
            this.publish(domainEvent);
        }
    }

    public void reset() {
        if (!this.isPublishing()) {
            this.setSubscribers(null);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void subscribe(DomainEventSubscriber<T> aSubscriber) {
        if (!this.isPublishing()) {
            this.ensureSubscribersList();

            this.subscribers().add(aSubscriber);
        }
    }

    public void mySubscribe(MyDomainEventSubscriber aSubscriber) {
        if (!this.isPublishing()) {
            this.ensureSubscribersList();
            //Log.e("Size befor insert", "" + subscribers().size());
            this.subscribers().add(aSubscriber);
        }
    }


    @SuppressWarnings("rawtypes")
    private void ensureSubscribersList() {
        if (!this.hasSubscribers()) {
            this.setSubscribers(new ArrayList());
        }
    }

    private boolean isPublishing() {
        return this.publishing;
    }

    private void setPublishing(boolean aFlag) {
        this.publishing = aFlag;
    }

    private boolean hasSubscribers() {
        return this.subscribers() != null;
    }

    @SuppressWarnings("rawtypes")
    public List subscribers() {
        return this.subscribers;
    }

    public List unmodifiableSubscribers() {

        return Collections.unmodifiableList(this.subscribers);
    }


    @SuppressWarnings("rawtypes")
    private void setSubscribers(List aSubscriberList) {
        this.subscribers = aSubscriberList;
    }
    
    
    public void unsubscribe(RessourceCreated ressourceCreated){
        List newList = new ArrayList();
        List<MyDomainEventSubscriber> allSubscribers = this.subscribers();

        for (MyDomainEventSubscriber subscriber : allSubscribers) {
           if (!subscriber.equals(ressourceCreated)){
               newList.add(subscriber);
           }
        }

        this.setSubscribers(newList);

    }

}
