<html>
	<head>
		<title>Bootstrap grid system</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style/smalltalkstyle.css">
		<style>
			.panel{
				display: flex;
			}
			@media(min-width:1200px){
				.content1{
					flex-basis:100px;
					flex-grow:1;
				}
				.content2{
					flex-basis:200px;
					flex-grow:1;
				}
				.panel{
					flex-wrap:nowrap;
				}
			}
			@media(max-width:1199px){
				.content1, .content2{
					flex-basis:100%;
					flex-shrink:0;
				}
				.panel{
					flex-wrap:wrap;
				}
			}
		</style>
	</head>
	
	<body>
		<div class="box background">
			<div>Page</div>
			<div class="box panel row">
				<div>Top</div>
				<div class="box content1">MenuItem 1</div>
				<div class="box content2">MenuItem 2</div>
				<div class="box content1">MenuItem 3</div>
			</div>
		</div>
	</body>

</html>
