package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.Problem;
import com.sajang.devracebackend.dto.problem.ProblemDto;
import com.sajang.devracebackend.repository.ProblemRepository;
import com.sajang.devracebackend.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;

    @Transactional
    //@Override
    public ProblemDto crawlProblem(Integer problemNumber) throws IOException{
        String Url = "https://www.acmicpc.net/problem/" + problemNumber;
        Document doc = Jsoup.connect(Url).get();

        //#problem-body에 접근
        Elements contents = doc.select("#problem-body");         //body에 접근
        Elements contentHead = doc.select(".page-header h1");          //문제 title 추출을 위한 page-header에 접근
        Elements sampleDataElement = doc.getElementsByClass("col-md-6");   //예제 문제쪽 요소에 따로 접근

        int  Amount = sampleDataElement.size();        //예제 문제 개수 추출

        JSONObject sampleInputJson = new JSONObject();
        JSONObject sampleOutputJson = new JSONObject();
        for(int i =1;i<=Amount/2;i++){
            sampleInputJson.put("sampleInput"+i,contents.select(".col-md-6 #sample-input-"+i).text()); //예제객체 담기
            sampleOutputJson.put("sampleOutput"+i,contents.select(".col-md-6 #sample-output-"+i).text()); //예제객체 담기
        }

        String sampleInput = sampleInputJson.toString(); //Json -> String
        String sampleOutput = sampleOutputJson.toString(); //Json -> String

        String problemTitle = contentHead.select("#problem_title").text();      //title 은 따로 접근. body가 아닌 header 부분
        ProblemDto data = ProblemDto.builder()                                        //Builder를 이용해 ProblemDto 값으로 저장
                .imageUrl(contents.select("p img").attr("abs:src"))
                .number(problemNumber)
                .title(problemTitle)
                .content(contents.select("#problem_description").toString())
                .problemInput(contents.select("#problem_input").toString())
                .problemOutput(contents.select("#problem_output").toString())
                .problemLimit(contents.select("#problem_limit").toString())
                .sampleInput(sampleInput)
                .sampleOutput(sampleOutput)
                .build();

        ProblemDto problemDto = new ProblemDto(
                data.getImageUrl(),
                data.getTitle(),
                data.getNumber(),
                data.getContent(),
                data.getProblemInput(),
                data.getProblemOutput(),
                data.getProblemLimit(),
                data.getSampleInput(),
                data.getSampleOutput()
        );

        Problem problemEntity = problemRepository.save(problemDto.toEntity());

        return new ProblemDto(problemEntity);
    }
}
