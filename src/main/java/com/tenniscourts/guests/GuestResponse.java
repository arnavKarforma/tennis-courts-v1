package com.tenniscourts.guests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Data
@EqualsAndHashCode
public class GuestResponse {

    private Long id;
    private String name;
}
