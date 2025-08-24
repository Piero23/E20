package com.example.donut;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DonutController {
    // docker run -d -p 9411:9411 openzipkin/zipkin
    // Invoke-WebRequest -Uri http://localhost:8888/donut | Select-Object -ExpandProperty Content
    @GetMapping("/donut")
    public String getDonut() {
        return "Donut is ready!" +
                "                                                                               \n" +
                "                                                                               \n" +
                "                                $@@@@$$$$$@$@$                                 \n" +
                "                            $$$$$#############$$$$                             \n" +
                "                          $$$####***!*!**!!!!**#####                           \n" +
                "                        #####****!!!!!====!!!!*!!**###                         \n" +
                "                      *####***!!!=;;;::::::;;===!!******                       \n" +
                "                     **#*****!!=;::~--,,,,--~:;;==!!****!                      \n" +
                "                    =******!!=;;:~-,.........-~::==!!!***=                     \n" +
                "                    !*****!!==;:~-............--:;=!!!!**!                     \n" +
                "                    !*****!!==;:-,..        ..,-:;==!!!*!!                     \n" +
                "                   ;=!!**!!!!==;~,.          .,~:;=!!!!!!=:                    \n" +
                "                   :=!!!!!!!!==;;:-          :=!!!*****!!=:                    \n" +
                "                    =!!!!!**!*!!===;        !**********!!=                     \n" +
                "                    ;=!!!!!************!!*##$$#####***!!=;                     \n" +
                "                    -;=!!!!*****####$$$$$@@@@$$$###**!!=;-                     \n" +
                "                     -;==!!!****###$$$$@@@@@$$$###**!!=;~                      \n" +
                "                      -:;==!!!****###$$$$$$$$###***!=;:-                       \n" +
                "                        ~:;==!!!!!****####*****!!!==;~                         \n" +
                "                          -~:;;==!!!*!*!!!***!!=;;:-.                          \n" +
                "                            .-~::;;========;;;:~~.                             \n" +
                "                                .,,--~~~----,.                                 ";
    }
}