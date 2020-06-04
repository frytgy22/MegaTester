package ua.test.mega.tester.adapters;

import org.springframework.stereotype.Component;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import ua.test.mega.tester.core.api.NotificationAdapter;
import ua.test.mega.tester.core.api.model.Notification;

@Component
public class NotificationReactiveStreamAdapter implements NotificationAdapter {

    private final FluxProcessor processor = DirectProcessor.create();//TODO

    @Override
    public void register(Notification notification) {
        processor.onNext(notification);
    }

    @Override
    public Flux<Notification> provideNotificationStream() {
        return processor;
    }
}
