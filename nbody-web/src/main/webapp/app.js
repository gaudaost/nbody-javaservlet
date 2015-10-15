$(document)
		.ready(
				function() {

					var scene = new THREE.Scene();
					var camera = new THREE.PerspectiveCamera(75,
							window.innerWidth / window.innerHeight, 0.1, 1000);

					var renderer = new THREE.WebGLRenderer();
					renderer.setSize(window.innerWidth, window.innerHeight);
					var container = document.getElementById('container');
					container.appendChild(renderer.domElement);
					var planets=[];
					var geometry = new THREE.BoxGeometry(1, 1, 1);
					var material = new THREE.MeshBasicMaterial({
						color : 0x00ff00
					});
					for (var i = 0; i < 4000; i++) {
						planets.push(new THREE.Mesh(geometry, material));
						scene.add(planets[i]);
					}
					
					//var cube = new THREE.Mesh(geometry, material);
					//scene.add(cube);
					camera.position.z = 500;
					function render() {
						requestAnimationFrame(render);
						renderer.render(scene, camera);
					}
					render();

					//Stops the submit request
					$("#myAjaxRequestForm").submit(function(e) {
						e.preventDefault();
					});
					request();
					//checks for the button click event
					function request() {
						$
								.ajax({
									type : "GET",
									url : "hello",
									dataType : "json",
									//if received a response from the server
									success : function(data, textStatus, jqXHR) {
										request();
										//our country code was correct so we have some information to display
										if (data.success) {
											$("#ajaxResponse").html("");
											$("#ajaxResponse").append(
													"<b>Count:</b> "
															+ data.count);
											for (var i = 0; i < 4000; i++) {
												planets[i].position.x=data.data[i].point[0];
												planets[i].position.y=data.data[i].point[1];
												planets[i].position.z=data.data[i].point[2];
											}
										} else {
											$("#ajaxResponse")
													.html(
															"<div><b>Country code in Invalid!</b></div>");
										}
									},

									//If there was no resonse from the server
									error : function(jqXHR, textStatus,
											errorThrown) {
										console
												.log("Something really bad happened "
														+ textStatus);
										$("#ajaxResponse").html(
												jqXHR.responseText);
									}
								});
					}

				});