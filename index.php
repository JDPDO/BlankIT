<html>
<head>
<meta content="charset=UTF-8"/>
<title>Autoquiz Backend</title>
</head>

<body>

<?php
#echo utf8_encode("echo \"".$_POST['text']."\" | /srv/http/WordCount | java -jar FindBlank.jar ".$_POST['fq']);
exec("echo \"".utf8_encode($_POST['text'])."\" | /srv/http/WordCount | java -jar FindBlank.jar ".utf8_encode($_POST['fq']),$output,$return);
foreach($output as $s){
	echo ($s."<br/>");
}
echo ("[@24]");
?>
</p>
</body>
</html>
