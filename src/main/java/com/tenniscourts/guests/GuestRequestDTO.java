package com.tenniscourts.guests;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Data
@EqualsAndHashCode
public class GuestRequestDTO {

    @NotNull
    private String name;

}
