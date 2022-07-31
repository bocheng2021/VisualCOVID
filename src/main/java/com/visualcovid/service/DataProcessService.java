package com.visualcovid.service;

import com.visualcovid.entity.ReliabilityData;
import com.visualcovid.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class DataProcessService {
    @Autowired
    private UserMapper mapper;

    @Transactional
    public void getAndProcessFlag()
    {
        double[] normal_frequency={0.301,0.176,0.125,0.097,0.079,0.067,0.058,0.051,0.046};
        ReliabilityData[] data = mapper.getAllReliabilityData();
        for (ReliabilityData per_data: data)
        {
            double[] freq_curr = {per_data.getFreq_1(),per_data.getFreq_2(),per_data.getFreq_3(),per_data.getFreq_4(),
                    per_data.getFreq_5(),per_data.getFreq_6(),per_data.getFreq_7(),per_data.getFreq_8(),per_data.getFreq_9()};
            double ssd, mad =0, d_factor,Cum_Sum_Of_Squares=0;
            int flag =0;
            for (int i=0;i<freq_curr.length;i++)
            {
                double raw_pow = Math.pow((normal_frequency[i] - freq_curr[i]), 2);
                Cum_Sum_Of_Squares = Cum_Sum_Of_Squares + raw_pow;
                mad = mad + Math.abs(normal_frequency[i]-freq_curr[i]);
            }
            ssd = Cum_Sum_Of_Squares * 10000;
            mad = mad/freq_curr.length;
            d_factor = Math.sqrt(Cum_Sum_Of_Squares)/1.03606;
            if (mad>0.015 && ssd>100 && d_factor>0.2)
            {
                flag =2;
            }
            else if (mad>0.015 || ssd>100 || d_factor>0.2)
            {
                flag =1;
            }
            mapper.updateReliabilityDataByCountry(per_data.getSlug(), flag, d_factor, ssd, mad);
            System.out.println(per_data.getName()+" "+ssd+" "+mad+" "+d_factor+" "+flag);
        }
    }
}
