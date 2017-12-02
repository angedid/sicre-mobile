package cm.mindef.sed.sicre.mobile.oservable;

import java.util.Date;

public interface DomainEvent {

    int eventVersion();

    Date occurredOn();
}
