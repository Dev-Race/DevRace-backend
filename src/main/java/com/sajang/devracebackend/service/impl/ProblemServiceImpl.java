package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.Problem;
import com.sajang.devracebackend.repository.ProblemRepository;
import com.sajang.devracebackend.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;


    @Transactional
    @Override
    public Problem crawlAndSaveProblem(Integer problemNumber) throws IOException {  // Service 클래스 내에서만 호출되며 방 생성에 이용될 것이기에 Dto가 아닌, Entity를 반환받음.
        String url = "https://www.acmicpc.net/problem/" + problemNumber;
        Document doc = Jsoup.connect(url).get();

        Elements contents = doc.select("#problem-body");  // #problem-body에 접근
        Elements contentHead = doc.select(".page-header h1");  // 문제 title 추출을 위한 page-header에 접근
        Elements sampleDataElement = doc.getElementsByClass("col-md-6");  // 예제 문제쪽 요소에 따로 접근

        int Amount = sampleDataElement.size();  // 예제 문제 개수 추출
        List<String> sampleInput = new ArrayList<>();
        List<String> sampleOutput = new ArrayList<>();
        for(int i=1; i<=Amount/2; i++) {
            sampleInput.add(contents.select(".col-md-6 #sample-input-"+i).text());  // 예제입력 객체 담기
            sampleOutput.add(contents.select(".col-md-6 #sample-output-"+i).text());  // 예제출력 객체 담기
        }

        String problemTitle = contentHead.select("#problem_title").text();  // title은 따로 접근. body가 아닌 header 부분
        String problemInput = contents.select("#problem_input").toString();
        String problemOutput = contents.select("#problem_output").toString();

        String problemLimit = contents.select("#problem_limit").toString();
        if(problemLimit.equals("<div id=\"problem_limit\" class=\"problem-text\">\n</div>")) problemLimit = null;  // 제한사항이 없는 경우, null로 저장.

        Elements problemDescription = contents.select("#problem_description");
        Elements images = problemDescription.select("img");  // 모든 img 태그를 찾음.
        for (Element img : images) {  // 각 이미지의 상대 경로를 절대 경로로 변경.
            String imageUrl = img.absUrl("src");
            img.attr("src", imageUrl);
        }
        String content = problemDescription.toString();

        Problem problem = Problem.ProblemSaveBuilder()
                .number(problemNumber)
                .title(problemTitle)
                .content(content)
                .problemInput(problemInput)
                .problemOutput(problemOutput)
                .problemLimit(problemLimit)
                .sampleInput(sampleInput)
                .sampleOutput(sampleOutput)
                .build();

        return problemRepository.save(problem);
    }
}