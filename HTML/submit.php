<?php

$name = $_POST["name"] ?? "";
$email = $_POST["email"] ?? "";
$phone = $_POST["phone"] ?? "";
$typeOfFeedback = $_POST["type-of-feedback"] ?? "";
$message = $_POST["message"] ?? "";

$file = fopen("/var/www/backend/submissions.csv", "a");
fputcsv($file, [$name, $phone, $email, $typeOfFeedback, $message, date("Y-m-d H:i:s")]);
fclose($file);

?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="refresh" content="2;url=/html/feedback.html">
    <title>Feedback submitted</title>
</head>
<body>
    <p>Thank you. Your feedback was submitted successfully.</p>
    <p>You will be sent back to the feedback page in 2 seconds.</p>
</body>
</html>