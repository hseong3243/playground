package com.seong.playground.lock;

import com.seong.playground.common.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(nullable = false)
    private Integer limitOfPeople;

    @Version
    private Long version;

    public Event(Integer limitOfPeople) {
        this.limitOfPeople = limitOfPeople;
    }

    public EventMember join(Member member, long joinedMember) {
        if(joinedMember >= limitOfPeople) {
            throw new RuntimeException("제한 인원이 가득찼습니다.");
        }
        return new EventMember(this, member);
    }

    public void validate(long joinedMember) {
        if(joinedMember >= limitOfPeople) {
            throw new RuntimeException();
        }
    }
}
