package com.tenniscourts.tenniscourts;

import java.util.List;

public interface TennisCourtService {
    TennisCourtDTO addTennisCourt(TennisCourtDTO tennisCourt);

    TennisCourtDTO findTennisCourtById(Long id);

    TennisCourtDTO findTennisCourtWithSchedulesById(Long tennisCourtId);

    List<TennisCourtDTO> listAllTennisCourts();
}
