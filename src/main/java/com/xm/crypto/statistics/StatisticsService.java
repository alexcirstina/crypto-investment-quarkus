package com.xm.crypto.statistics;

import com.xm.crypto.dto.NormalizedRangeDto;
import com.xm.crypto.dto.StatisticsDto;

import java.util.List;

public interface StatisticsService {

    StatisticsDto generalStats(String symbol);
    List<NormalizedRangeDto> normalizedRangeDesc(String date);

}
