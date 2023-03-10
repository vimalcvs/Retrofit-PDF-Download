<?php
$db = new mysqli(
    "localhost",
    "u726159739_test",
    "/cEK+Q^&|jA0",
    "u726159739_test"
);

if ($db->connect_errno) {
    die("Failed to connect to MySQL: " . $db->connect_error);
}

$category = isset($_GET["category"]) ? $db->real_escape_string($_GET["category"]) : "";
$amount = isset($_GET["amount"]) ? intval($_GET["amount"]) : 10;
$category_condition = $category ? "category = '$category' AND" : "";

$query = "SELECT category, question, correct_answer, a, b, c, d FROM quiz_aptitude WHERE $category_condition 1=1
          UNION
          SELECT category, question, correct_answer, a, b, c, d FROM quiz_art WHERE $category_condition 1=1
           UNION
          SELECT category, question, correct_answer, a, b, c, d FROM quiz_computer WHERE $category_condition 1=1
           UNION
          SELECT category, question, correct_answer, a, b, c, d FROM quiz_economy WHERE $category_condition 1=1
           UNION
          SELECT category, question, correct_answer, a, b, c, d FROM quiz_geography WHERE $category_condition 1=1
           UNION
          SELECT category, question, correct_answer, a, b, c, d FROM quiz_hindi WHERE $category_condition 1=1
           UNION
          SELECT category, question, correct_answer, a, b, c, d FROM quiz_history WHERE $category_condition 1=1
           UNION
          SELECT category, question, correct_answer, a, b, c, d FROM quiz_maths WHERE $category_condition 1=1
           UNION
          SELECT category, question, correct_answer, a, b, c, d FROM quiz_politics WHERE $category_condition 1=1
            UNION
          SELECT category, question, correct_answer, a, b, c, d FROM quiz_random WHERE $category_condition 1=1
            UNION
          SELECT category, question, correct_answer, a, b, c, d FROM quiz_science WHERE $category_condition 1=1
             UNION
          SELECT category, question, correct_answer, a, b, c, d FROM quiz_sports WHERE $category_condition 1=1
          ORDER BY RAND() LIMIT $amount";

$result = $db->query($query);

$data = [];
while ($row = $result->fetch_assoc()) {
    $incorrectAnswers = [];
    for ($i = "a"; $i <= "d"; $i++) {
        if ($row["correct_answer"] != $i) {
            $incorrectAnswers[] = ucwords($row[$i]);
        }
    }
    shuffle($incorrectAnswers);
    $data[] = [
        "category" => $row["category"],
        "question" => $row["question"],
        "correct_answer" => ucwords($row[$row["correct_answer"]]),
        "incorrect_answers" => $incorrectAnswers,
    ];
}

$response = [
    "response_code" => 0,
    "results" => $data,
];

header("Content-Type: application/json");
echo json_encode($response, JSON_UNESCAPED_UNICODE);
?>
