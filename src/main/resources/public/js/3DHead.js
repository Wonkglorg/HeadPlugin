import * as THREE from '../../../../../../three.module.js';
import {OrbitControls} from '../../three/examples/jsm/controls/OrbitControls.js';

const canvas = document.getElementById('canvas');
const renderer = new THREE.WebGLRenderer({canvas});

const scene = new THREE.Scene();

const camera = new THREE.PerspectiveCamera(75, canvas.width / canvas.height, 0.1, 1000);
camera.position.set(0, 0, 5);

const controls = new OrbitControls(camera, canvas);
controls.enableDamping = true;

const textureLoader = new THREE.TextureLoader();
const base64Texture = 'data:image/png;base64,eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjA5ODM5N2EyNzBiNGMzZDJiMWU1NzRiOGNmZDNjYzRlYTM0MDkwNjZjZWZlMzFlYTk5MzYzM2M5ZDU3NiJ9fX0=';
const texture = textureLoader.load(base64Texture, () =>
{
    texture.wrapS = texture.wrapT = THREE.RepeatWrapping;
    console.error('Loaded image');
}, () =>
{
    console.error('Progress');
}, () =>
{
    console.error('Texture not loaded');
});

canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

const material = new THREE.MeshBasicMaterial({map: texture});

const geometry = new THREE.BoxGeometry(1, 1, 1);
geometry.faceVertexUvs[0][0][0].set(0, 0);
geometry.faceVertexUvs[0][0][1].set(1, 0);
geometry.faceVertexUvs[0][0][2].set(1, 1);
geometry.faceVertexUvs[0][1][0].set(0, 0);
geometry.faceVertexUvs[0][1][1].set(1, 1);
geometry.faceVertexUvs[0][1][2].set(0, 1);

const mesh = new THREE.Mesh(geometry, material);
scene.add(mesh);

function animate()
{
    requestAnimationFrame(animate);

    controls.update();

    renderer.render(scene, camera);
}

animate();